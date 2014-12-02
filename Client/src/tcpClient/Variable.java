package tcpClient;

public class Variable {
	private String placa;
	private String nombre;
	private String funcPrincipal;
	private String estado;
	private String ultimaAccion;
	
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getFuncPrincipal() {
		return funcPrincipal;
	}
	public void setFuncPrincipal(String funcPrincipal) {
		this.funcPrincipal = funcPrincipal;
	}
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getUltimaAccion() {
		return ultimaAccion;
	}
	public void setUltimaAccion(String ultimaAccion) {
		this.ultimaAccion = ultimaAccion;
	}
}
