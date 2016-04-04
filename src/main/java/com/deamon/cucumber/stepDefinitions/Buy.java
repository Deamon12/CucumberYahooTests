package com.deamon.cucumber.stepDefinitions;

/**
 * Parse buy criteria with input as follows:
 * | largePE | largePrice | largeYield | medPE | medPrice | medYield | smPE | smPrice | smYield |
 * 
 * params: PE is the PEratio requirement
 * 		   price is the percent to use of the 50 week average
 * 		   Yield is the DividendYield requirement
 * 
 * Variable with Comp in the name, are the comparison operators (ie l = <, g = >)
 * 
 */

public class Buy {

		private String largePE;
	    private String largePrice;
	    private String largeYield;
		
	    private String medPE;
	    private String medPrice;
	    private String medYield;
	    
	    private String smPE;
	    private String smPrice;
	    private String smYield;

		private String lPEComp;
	    private double largePEdub;
	    private String lPriceComp;
	    private double largePricedub;
	    private String lYieldComp;
	    private double largeYielddub;
	    
	    private String mPEComp;
	    private double medPEdub;
	    private String mPriceComp;
	    private double medPricedub;
	    private String mYieldComp;
	    private double medYielddub;
	    
	    private String sPEComp;
	    private double smPEdub;
	    private String sPriceComp;
	    private double smPricedub;
	    private String sYieldComp;
	    private double smYielddub;
	    

		public void analyze(){
			
			this.lPEComp = largePE.substring(0, 1);
			this.largePEdub = Double.parseDouble(largePE.substring(1));
			this.lPriceComp = largePrice.substring(0, 1);
			this.largePricedub = Double.parseDouble(largePrice.substring(1))*.01;
			this.lYieldComp = largeYield.substring(0, 1);
			this.largeYielddub = Double.parseDouble(largeYield.substring(1));
			
			this.mPEComp = medPE.substring(0, 1);
			this.medPEdub = Double.parseDouble(medPE.substring(1));
			this.mPriceComp = medPrice.substring(0, 1);
			this.medPricedub = Double.parseDouble(medPrice.substring(1))*.01;
			this.mYieldComp = medYield.substring(0, 1);
			this.medYielddub = Double.parseDouble(medYield.substring(1));
			
			this.sPEComp = smPE.substring(0, 1);
			this.smPEdub = Double.parseDouble(smPE.substring(1));
			this.sPriceComp = smPrice.substring(0, 1);
			this.smPricedub = Double.parseDouble(smPrice.substring(1))*.01;
			this.sYieldComp = smYield.substring(0, 1);
			this.smYielddub = Double.parseDouble(smYield.substring(1));
			
			
		}
		
	    
		public String getlPEComp() {
			return lPEComp;
		}


		public String getlPriceComp() {
			return lPriceComp;
		}


		public String getlYieldComp() {
			return lYieldComp;
		}


		public String getmPEComp() {
			return mPEComp;
		}


		public String getmPriceComp() {
			return mPriceComp;
		}


		public String getmYieldComp() {
			return mYieldComp;
		}


		public String getsPEComp() {
			return sPEComp;
		}


		public String getsPriceComp() {
			return sPriceComp;
		}


		public String getsYieldComp() {
			return sYieldComp;
		}
		
		
		public double getLargePEdub() {
			return largePEdub;
		}


		public double getLargePricedub() {
			return largePricedub;
		}


		public double getLargeYielddub() {
			return largeYielddub;
		}


		public double getMedPEdub() {
			return medPEdub;
		}


		public double getMedPricedub() {
			return medPricedub;
		}


		public double getMedYielddub() {
			return medYielddub;
		}


		public double getSmPEdub() {
			return smPEdub;
		}


		public double getSmPricedub() {
			return smPricedub;
		}


		public double getSmYielddub() {
			return smYielddub;
		}


		@Override
		public String toString() {
			return "Buy [lPEComp=" + lPEComp + ", largePEdub=" + largePEdub
					+ ", lPriceComp=" + lPriceComp + ", largePricedub="
					+ largePricedub + ", lYieldComp=" + lYieldComp
					+ ", largeYielddub=" + largeYielddub + ", mPEComp="
					+ mPEComp + ", medPEdub=" + medPEdub + ", mPriceComp="
					+ mPriceComp + ", medPricedub=" + medPricedub
					+ ", mYieldComp=" + mYieldComp + ", medYielddub="
					+ medYielddub + ", sPEComp=" + sPEComp + ", smPEdub="
					+ smPEdub + ", sPriceComp=" + sPriceComp + ", smPricedub="
					+ smPricedub + ", sYieldComp=" + sYieldComp
					+ ", smYielddub=" + smYielddub + "]";
		}


		

}
