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

import java.util.*;

public class PCICellFactory {
	
	private final int numberCells;
	

	public PCICellFactory(final int numberCells) {
		this.numberCells = numberCells;
	}
	
	private final Random random = new Random();
	
	public List<PCICell> getCellList() {
		final List<PCICell> cellList = new ArrayList<PCICell>();
		for(int i=0;i<numberCells;i++) 
		{
			final int randomInt = random.nextInt(numberCells);
			final String cellFDN = "CellFDN="+randomInt;
			final String meContext = "MeContext="+randomInt;
			cellList.add(new PCICell(null, cellFDN, meContext, null, null, random.nextDouble(), random.nextDouble(),
					random.nextDouble(), random.nextDouble(), randomInt, random.nextDouble(), random.nextDouble(), null));
		}
		return cellList;
	}
	
	public Map<Long,PCICell> getPUTDetails() {
		Map<Long,PCICell> map = new HashMap<Long,PCICell>(1);
		Long k = random.nextLong();
		final int randomInt = random.nextInt(numberCells);
		final String cellFDN = "CellFDN="+randomInt;
		final String meContext = "MeContext="+randomInt;
		PCICell cell = new PCICell(null, cellFDN, meContext, null, null, random.nextDouble(), random.nextDouble(),
				random.nextDouble(), random.nextDouble(), randomInt, random.nextDouble(), random.nextDouble(), null);
		map.put(k,cell);
		return map;
	}
}