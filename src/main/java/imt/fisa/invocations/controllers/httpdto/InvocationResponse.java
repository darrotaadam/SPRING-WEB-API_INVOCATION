package imt.fisa.invocations.controllers.httpdto;

public class InvocationResponse {
    private String monstreId;

    public InvocationResponse(String monstreId) {
        this.monstreId = monstreId;
    }

    public String getMonstreId() {
        return monstreId;
    }

    public void setMonstreId(String monstreId) {
        this.monstreId = monstreId;
    }
}
