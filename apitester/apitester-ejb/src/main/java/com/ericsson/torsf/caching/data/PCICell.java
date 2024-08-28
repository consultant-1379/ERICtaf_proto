/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.torsf.caching.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PCICell implements Serializable {

		private static final long serialVersionUID = 6839364916245416928L;
	    
		protected final EPCICellType cellType;

	    protected final String cellFDN;

	    protected CellTrafficId cellTrafficId;

	    protected PCI pci;

	    protected final String meContextForCell;

	    protected double utranFrequency;

	    protected double maxTransPwr;

	    /**
	     * Value stored in radian's
	     */
	    protected double latitude;

	    /**
	     * Value stored in radian's
	     */
	    protected double longitude;

	    protected int antennaHeight;

	    protected double antennaGain;

	    protected double antennaBearing;

	    protected Map<String, NeighbourType> neighbourCellRefs;

	    public PCICell(final EPCICellType cellType, final String cellFDN, final String meContextForCell,
	            final CellTrafficId cellTrafficId, final PCI pci, final double utranFrequency, final double maxTransPwr,
	            final double latitude, final double longitude, final int antennaHeight, final double antennaBearing,
	            final double antennaGain, final Map<String, NeighbourType> neighbourCellRefs) {
	        this.cellType = cellType;
	        this.cellFDN = cellFDN;
	        this.cellTrafficId = cellTrafficId;
	        this.pci = pci;
	        this.meContextForCell = meContextForCell;
	        this.utranFrequency = utranFrequency;
	        this.maxTransPwr = maxTransPwr;
	        this.latitude = latitude;
	        this.longitude = longitude;
	        this.antennaHeight = antennaHeight;
	        this.antennaGain = antennaGain;
	        this.antennaBearing = antennaBearing;
	        this.neighbourCellRefs = retrieveFdnNeighbourTypeMap(neighbourCellRefs);
	    }

	    final protected Map<String, NeighbourType> retrieveFdnNeighbourTypeMap(final Map<String, NeighbourType> mapToCopy) {
	        HashMap<String, NeighbourType> newMap;

	        if (mapToCopy == null) {
	            newMap = new HashMap<String, NeighbourType>();
	        } else {
	            newMap = new HashMap<String, NeighbourType>(mapToCopy);
	        }
	        return newMap;
	    }

	    public EPCICellType getCellType() {
	        return cellType;
	    }

	    /**
	     * <!-- begin-user-doc --> <!-- end-user-doc -->
	     * 
	     * @return the cellFDN
	     * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	     */
	    public String getCellFDN() {
	        return cellFDN;
	    }

	    /**
	     * <!-- begin-user-doc --> <!-- end-user-doc -->
	     * 
	     * @return the cellTrafficId
	     * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	     */
	    public CellTrafficId getCellTrafficId() {
	        return cellTrafficId;
	    }

	    /**
	     * <!-- begin-user-doc --> <!-- end-user-doc -->
	     * 
	     * @return the physicalLayerSubCellId
	     * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	     */
	    public int getPhysicalLayerSubCellId() {
	        return pci.getPhysicalLayerSubCellId();
	    }

	    /**
	     * <!-- begin-user-doc --> <!-- end-user-doc -->
	     * 
	     * @return the physicalLayerCellIdGroup
	     * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	     */
	    public int getPhysicalLayerCellIdGroup() {
	        return pci.getPhysicalLayerCellIdGroup();
	    }

	    /**
	     * <!-- begin-user-doc --> <!-- end-user-doc -->
	     * 
	     * @return the utranFrequency
	     * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	     */
	    public double getUtranFrequency() {
	        return utranFrequency;
	    }

	    /**
	     * <!-- begin-user-doc --> <!-- end-user-doc -->
	     * 
	     * @return the maxTransmissionPwr
	     * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	     */
	    public double getMaxTransmissionPwr() {
	        return maxTransPwr;
	    }

	    /**
	     * <!-- begin-user-doc --> <!-- end-user-doc -->
	     * 
	     * @return the longitude
	     * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	     */
	    public double getLongitude() {
	        return longitude;
	    }

	    /**
	     * <!-- begin-user-doc --> <!-- end-user-doc -->
	     * 
	     * @return the latitude
	     * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	     */
	    public double getLatitude() {
	        return latitude;
	    }

	    /**
	     * <!-- begin-user-doc --> <!-- end-user-doc -->
	     * 
	     * @return the antennaHeight
	     * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	     */
	    public int getAntennaHeight() {
	        return antennaHeight;
	    }

	    /**
	     * <!-- begin-user-doc --> <!-- end-user-doc -->
	     * 
	     * @return the antennaGain
	     * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	     */
	    public double getAntennaGain() {
	        return antennaGain;
	    }

	    /**
	     * <!-- begin-user-doc --> <!-- end-user-doc -->
	     * 
	     * @return the antennaBearing
	     * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	     */
	    public double getAntennaBearing() {
	        return antennaBearing;
	    }

	    /**
	     * <!-- begin-user-doc --> <!-- end-user-doc -->
	     * 
	     * @return the neighbourCellRefs
	     * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	     */
	    public Map<String, NeighbourType> getNeighbourCellRefs() {
	        return neighbourCellRefs;
	    }

	    public PCI getPci() {
	        return pci;
	    }

	    public String getMeContextForCell() {
	        return meContextForCell;
	    }

	    @Override
	    public String toString() {
	        final StringBuffer strbuf = new StringBuffer(300);
	        strbuf.append("FDN: ").append(getCellFDN()).append('\n');
	        strbuf.append("cellType: ").append(getCellType()).append('\n');
	        strbuf.append("cellTrafficId: ").append(getCellTrafficId()).append('\n');
	        strbuf.append("pci: ").append(getPci()).append('\n');
	        strbuf.append("MeContext: ").append(getMeContextForCell()).append('\n');
	        strbuf.append("utranFrequency: ").append(getUtranFrequency()).append('\n');
	        strbuf.append("maxTransmissionPwr: ").append(getMaxTransmissionPwr()).append('\n');
	        strbuf.append("latitude: ").append(getLatitude()).append('\n');
	        strbuf.append("longitude: ").append(getLongitude()).append('\n');
	        strbuf.append("antennaHeight: ").append(getAntennaHeight()).append('\n');
	        strbuf.append("antennaGain: ").append(getAntennaGain()).append('\n');
	        strbuf.append("antennaBearing: ").append(getAntennaBearing()).append('\n');
	        strbuf.append("number of neighbors: ").append(getNeighbourCellRefs().size()).append('\n');

	        return strbuf.toString();
	    }

}
