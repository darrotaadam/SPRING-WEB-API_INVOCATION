package imt.fisa.invocations;

import imt.fisa.invocations.exceptions.InternalServerErrorException;
import imt.fisa.invocations.persistence.dto.MonsterTemplateEntity;
import imt.fisa.invocations.persistence.repositories.MonsterTemplateRepository;
import imt.fisa.invocations.services.InvocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvocationsApplicationTests {

	@Mock
	private MonsterTemplateRepository monsterTemplateRepository;

	@InjectMocks
	private InvocationService invocationService;

	// Les 4 monstres du JSON avec leurs lootRates réels
	private MonsterTemplateEntity monsterFire;   // lootRate = 0.3  → 30%
	private MonsterTemplateEntity monsterWind;   // lootRate = 0.3  → 30%
	private MonsterTemplateEntity monsterWater1; // lootRate = 0.3  → 30%
	private MonsterTemplateEntity monsterWater2; // lootRate = 0.1  → 10%

	private static final int ITERATIONS   = 100_000;
	private static final double TOLERANCE = 0.03; // ±3 points de pourcentage

	@BeforeEach
	void setUp() {
		monsterFire   = new MonsterTemplateEntity("1", "Fire Monster",   "fire",  1200, 450, 300, 85, null, 1, 0, 0.3);
		monsterWind   = new MonsterTemplateEntity("2", "Wind Monster",   "wind",  1500, 200, 450, 80, null, 1, 0, 0.3);
		monsterWater1 = new MonsterTemplateEntity("3", "Water Monster 1","water", 2500, 150, 200, 70, null, 1, 0, 0.3);
		monsterWater2 = new MonsterTemplateEntity("4", "Water Monster 2","water", 1200, 550, 350, 80, null, 1, 0, 0.1);
	}

	// -------------------------------------------------------------------------
	// BENCHMARK LOOT RATE
	// -------------------------------------------------------------------------

	/**
	 * Benchmark principal : sur 100 000 invocations, chaque monstre doit apparaître
	 * avec une fréquence proche de son lootRate déclaré (tolérance ±3%).
	 *
	 * Monstres et lootRates attendus :
	 *  - Fire   (id=1) : 0.3  → ~30%
	 *  - Wind   (id=2) : 0.3  → ~30%
	 *  - Water1 (id=3) : 0.3  → ~30%
	 *  - Water2 (id=4) : 0.1  → ~10%
	 */
	@Test
	void testBenchmarkLootRate_distributionConforme() {
		List<MonsterTemplateEntity> templates = List.of(monsterFire, monsterWind, monsterWater1, monsterWater2);
		when(monsterTemplateRepository.findAll()).thenReturn(templates);

		Map<String, Integer> compteurs = new HashMap<>();
		for (MonsterTemplateEntity t : templates) {
			compteurs.put(t.getId(), 0);
		}

		for (int i = 0; i < ITERATIONS; i++) {
			MonsterTemplateEntity picked = invocationService.pickMonster();
			compteurs.merge(picked.getId(), 1, Integer::sum);
		}

		System.out.println("\n===== BENCHMARK LOOT RATE (" + ITERATIONS + " tirages) =====");
		for (MonsterTemplateEntity t : templates) {
			double observedRate = (double) compteurs.get(t.getId()) / ITERATIONS;
			System.out.printf("  %-20s (lootRate=%.1f) → observé=%.2f%%  (attendu=%.0f%%)%n",
					t.getName(), t.getLootRate(), observedRate * 100, t.getLootRate() * 100);

			assertThat(observedRate)
					.as("Taux observé pour '%s' (lootRate=%.1f) doit être proche de %.1f (±%.0f%%)",
							t.getName(), t.getLootRate(), t.getLootRate(), TOLERANCE * 100)
					.isCloseTo(t.getLootRate(), within(TOLERANCE));
		}
		System.out.println("=================================================\n");
	}

	/**
	 * Vérifie que la somme des lootRates est bien 1.0 (condition nécessaire pour
	 * que la probabilité totale soit cohérente).
	 */
	@Test
	void testSommeLootRates_egaleA1() {
		List<MonsterTemplateEntity> templates = List.of(monsterFire, monsterWind, monsterWater1, monsterWater2);

		double somme = templates.stream()
				.mapToDouble(MonsterTemplateEntity::getLootRate)
				.sum();

		System.out.printf("Somme des lootRates : %.1f%n", somme);
		assertThat(somme).isCloseTo(1.0, within(0.001));
	}

	/**
	 * Avec un seul monstre en base, pickMonster() doit toujours retourner ce monstre,
	 * quel que soit le résultat de Math.random().
	 */
	@Test
	void testPickMonster_monstreUnique_retourneToujoursLeMeme() {
		when(monsterTemplateRepository.findAll()).thenReturn(List.of(monsterFire));

		for (int i = 0; i < 1_000; i++) {
			MonsterTemplateEntity result = invocationService.pickMonster();
			assertThat(result.getId())
					.as("Avec un seul monstre, pickMonster() doit toujours retourner ce monstre")
					.isEqualTo("1");
		}
	}

	/**
	 * Avec deux monstres dont les lootRates sont très déséquilibrés (0.01 vs 0.99),
	 * la distribution observée doit refléter ce déséquilibre.
	 */
	@Test
	void testBenchmarkLootRate_desequilibre() {
		MonsterTemplateEntity rare   = new MonsterTemplateEntity("R", "Rare",   "fire", 100, 100, 100, 100, null, 1, 0, 0.01);
		MonsterTemplateEntity commun = new MonsterTemplateEntity("C", "Commun", "wind", 100, 100, 100, 100, null, 1, 0, 0.99);

		when(monsterTemplateRepository.findAll()).thenReturn(List.of(rare, commun));

		int countRare = 0;
		for (int i = 0; i < ITERATIONS; i++) {
			if ("R".equals(invocationService.pickMonster().getId())) countRare++;
		}

		double rateRare = (double) countRare / ITERATIONS;
		System.out.printf("Monstre rare observé : %.2f%% (attendu ~1%%)%n", rateRare * 100);

		assertThat(rateRare).isCloseTo(0.01, within(0.005)); // tolérance ±0.5%
	}

	// -------------------------------------------------------------------------
	// CAS LIMITES
	// -------------------------------------------------------------------------

	/**
	 * Si aucun monstre n'existe en base, pickMonster() doit lever
	 * une InternalServerErrorException plutôt que de renvoyer null.
	 */
	@Test
	void testPickMonster_listeVide_leveInternalServerError() {
		when(monsterTemplateRepository.findAll()).thenReturn(Collections.emptyList());

		assertThatThrownBy(() -> invocationService.pickMonster())
				.isInstanceOf(InternalServerErrorException.class)
				.as("Une liste vide doit lever InternalServerErrorException");
	}

	/**
	 * pickMonster() ne doit jamais retourner null quelle que soit la configuration.
	 */
	@Test
	void testPickMonster_neRetourneJamaisNull() {
		List<MonsterTemplateEntity> templates = List.of(monsterFire, monsterWind, monsterWater1, monsterWater2);
		when(monsterTemplateRepository.findAll()).thenReturn(templates);

		for (int i = 0; i < 10_000; i++) {
			assertThat(invocationService.pickMonster())
					.as("pickMonster() ne doit jamais retourner null")
					.isNotNull();
		}
	}

	/**
	 * pickMonster() ne doit retourner que des monstres présents dans la liste fournie.
	 */
	@Test
	void testPickMonster_retourneSeulementMonstresConnus() {
		List<MonsterTemplateEntity> templates = List.of(monsterFire, monsterWind, monsterWater1, monsterWater2);
		List<String> idsAttendus = List.of("1", "2", "3", "4");
		when(monsterTemplateRepository.findAll()).thenReturn(templates);

		for (int i = 0; i < 10_000; i++) {
			String id = invocationService.pickMonster().getId();
			assertThat(idsAttendus)
					.as("L'id '%s' retourné doit appartenir à la liste des templates", id)
					.contains(id);
		}
	}
}