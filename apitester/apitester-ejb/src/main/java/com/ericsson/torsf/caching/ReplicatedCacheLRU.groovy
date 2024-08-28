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
package com.ericsson.torsf.caching;

import java.io.Serializable;
import java.util.List;

import javax.cache.Cache;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.enterprise.context.ApplicationScoped;
import javax.ejb.Stateless;
import javax.annotation.PostConstruct;

import com.ericsson.oss.itpf.sdk.cache.CacheMode;
import com.ericsson.oss.itpf.sdk.cache.EvictionStrategy;
import com.ericsson.oss.itpf.sdk.cache.annotation.NamedCache;
import com.ericsson.oss.itpf.sdk.cache.modeling.annotation.ModeledNamedCache;

import com.ericsson.torsf.caching.data.PCICell;
import com.ericsson.torsf.caching.data.PCICellFactory;


   @ApplicationScoped
   public class ReplicatedCacheLRU implements Serializable {

	   private static final long serialVersionUID = -5037490765517629642L;
	   
	   @Inject
	   @NamedCache("ReplicatedCacheLRU")
	   private Cache<Long, PCICell> cache;
   
	   
	   @PostConstruct
	   	public void init() {
	   		System.out.println("------------------------------------I am here "+ cache);
	   	}
	   
	   public void load(String Cells) {
		   int numberOfCells = Integer.parseInt(Cells);
		   PCICellFactory factory = new PCICellFactory(numberOfCells);
		   List cellList = factory.getCellList();
		   for(long i=0;i<numberOfCells;i++)
		   {
			   cache.put(i, (PCICell) cellList.get((int) i));
		   }
		   
	   }
   
	   public String getPCICellSize(String cells, String cycles) {
		   
		   long totalCellSize = 0
		   int numberOfCells = Integer.parseInt(cells);
		   int numberOfCycles = Integer.parseInt(cycles);
		   
		   for (i in 0..numberOfCycles) {
		   System.gc();
		   System.runFinalization();
		   try {
		   Thread.sleep(1000);
		   long before = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		   
		   
		   load(cells)
		   
		   System.gc();
		   System.runFinalization();
		   Thread.sleep(1000);
		   long after = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		   def totalBytes = after - before;
		   long cellSize = (totalBytes/numberOfCells)
		   totalCellSize += cellSize
		   System.out.println("++++++++++++++++++ Cycle ${i} : PCICell object is ${cellSize} bytes ++++++++++++++++++++++++++")
		   } catch (Exception ex){
//				ignoring this on purpose - for now!!
		   }
		   }
		   long avSize = totalCellSize/Long.parseLong(cycles)
		   System.out.println("++++++++++++++++++ Average PCICell object is ${avSize} bytes ++++++++++++++++++++++++++")
		   return avSize.toString();
	   }
	   
	   public String name(String something) {
		   return "replicatedCache_LRU";
	   }
	   
	   public String name() {
		   return "ReplicatedCacheLRU";
	   }
	   public Object get(String key) {
		   return cache.get(Long.parseLong(key));
	   }
   
	   private void put(Long key, Object value) {
		   cache.put(key, (PCICell) value);
	   }
   
	   public void delete(Long key) {
		   cache.remove(key);
	   }
}