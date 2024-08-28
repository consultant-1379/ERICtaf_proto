package com.ericsson.cifwk.taf.api.prototype;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;





public class DataBasketTest extends Arquillian {


	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive jar =   ShrinkWrap.create(JavaArchive.class, "test.jar")
				.addClasses(Basket.class, OrderRepository.class, SingletonOrderRepository.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		return jar;
	}

	@Inject
	Basket basket;

	@EJB
	OrderRepository repo;

	@Test
	public void place_order_should_add_order() {
		basket.addItem("sunglasses");
		basket.addItem("suit");
		basket.placeOrder();
		Assert.assertEquals(1, repo.getOrderCount());
		Assert.assertEquals(0, basket.getItemCount());

		basket.addItem("raygun");
		basket.addItem("spaceship");
		basket.placeOrder();

		Assert.assertEquals(2, repo.getOrderCount());
		Assert.assertEquals(0, basket.getItemCount());
	}

	    @Test
	public void order_should_be_persistent() {
		Assert.assertEquals(2, repo.getOrderCount());
	}
}
