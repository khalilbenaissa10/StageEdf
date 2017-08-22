package tn.insat.models;

import java.io.Serializable;


public class Consommation implements Serializable {
	
	private  String idClient;
    private  String ville;
    private double conso;
    private  long timeStamp;
    
	public String getIdClient() {
		return idClient;
	}
	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public double getConso() {
		return conso;
	}
	public void setConso(String conso) {
		double cons = Double.valueOf(conso);
        if (cons > 0) {
            this.conso = cons;
        }else {
            this.conso = Math.abs(cons);
        }
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		 try {
	            long l = Long.valueOf(timeStamp);
	            this.timeStamp = l;
	        } catch (NumberFormatException nfe) {
	            System.out.println("NumberFormatException: " + nfe.getMessage());
	        }
	}
	
	
	public Consommation(String idClient, String ville, double conso,
			long timeStamp) {
		super();
		this.idClient = idClient;
		this.ville = ville;
		this.conso = conso;
		this.timeStamp = timeStamp;
	}
    
    public Consommation(){
    	
    }
    
    
    public Consommation(String s){
    	  String[] temp;
          String delimit = ";" ;
          temp = s.split(delimit);
          this.setIdClient(temp[0]);
          this.setConso(temp[1]);
          this.setVille(temp[2]);
          this.setTimeStamp(temp[5]);
    }
	@Override
	public String toString() {
		return "Consommation [idClient=" + idClient + ", ville=" + ville
				+ ", consommation=" + conso + ", timeStamp=" + timeStamp + "]";
	}
    
    

}
