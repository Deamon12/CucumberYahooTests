package com.deamon.cucumber.stepDefinitions;

/**
 * | largePE | largePrice | medPE | medPrice | smPE | smPrice |
 * @author Deagan M
 *
 */
public class Sell {

	private String largePE;
    private String largePrice;
	
    private String medPE;
    private String medPrice;
    
    private String smPE;
    private String smPrice;
	
    private String lPEComp;
    private double largePEdub;
    private String lPriceComp;
    private double largePricedub;
    
    private String mPEComp;
    private double medPEdub;
    private String mPriceComp;
    private double medPricedub;
    
    private String sPEComp;
    private double smPEdub;
    private String sPriceComp;
    private double smPricedub;
    
    public void analyze(){
		
		this.lPEComp = largePE.substring(0, 1);
		this.largePEdub = Double.parseDouble(largePE.substring(1))*.01;
		this.lPriceComp = largePrice.substring(0, 1);
		this.largePricedub = Double.parseDouble(largePrice.substring(1))*.01;
		
		this.mPEComp = medPE.substring(0, 1);
		this.medPEdub = Double.parseDouble(medPE.substring(1))*.01;
		this.mPriceComp = medPrice.substring(0, 1);
		this.medPricedub = Double.parseDouble(medPrice.substring(1))*.01;
		
		this.sPEComp = smPE.substring(0, 1);
		this.smPEdub = Double.parseDouble(smPE.substring(1))*.01;
		this.sPriceComp = smPrice.substring(0, 1);
		this.smPricedub = Double.parseDouble(smPrice.substring(1))*.01;
		
    }

	public String getlPEComp() {
		return lPEComp;
	}

	public double getLargePEdub() {
		return largePEdub;
	}

	public String getlPriceComp() {
		return lPriceComp;
	}

	public double getLargePricedub() {
		return largePricedub;
	}

	public String getmPEComp() {
		return mPEComp;
	}

	public double getMedPEdub() {
		return medPEdub;
	}

	public String getmPriceComp() {
		return mPriceComp;
	}

	public double getMedPricedub() {
		return medPricedub;
	}

	public String getsPEComp() {
		return sPEComp;
	}

	public double getSmPEdub() {
		return smPEdub;
	}

	public String getsPriceComp() {
		return sPriceComp;
	}

	public double getSmPricedub() {
		return smPricedub;
	}

	@Override
	public String toString() {
		return "Sell [lPEComp=" + lPEComp + ", largePEdub=" + largePEdub
				+ ", lPriceComp=" + lPriceComp + ", largePricedub="
				+ largePricedub + ", mPEComp=" + mPEComp + ", medPEdub="
				+ medPEdub + ", mPriceComp=" + mPriceComp + ", medPricedub="
				+ medPricedub + ", sPEComp=" + sPEComp + ", smPEdub=" + smPEdub
				+ ", sPriceComp=" + sPriceComp + ", smPricedub=" + smPricedub
				+ "]";
	}
		
	
	
	
}
