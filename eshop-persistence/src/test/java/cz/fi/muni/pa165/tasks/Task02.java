package cz.fi.muni.pa165.tasks;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.validation.ConstraintViolationException;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cz.fi.muni.pa165.PersistenceSampleApplicationContext;
import cz.fi.muni.pa165.entity.Category;
import cz.fi.muni.pa165.entity.Product;

import static org.testng.Assert.assertEquals;


@ContextConfiguration(classes = PersistenceSampleApplicationContext.class)
public class Task02 extends AbstractTestNGSpringContextTests {

	@PersistenceUnit
	private EntityManagerFactory emf;

	private Category electro = new Category();
	private Category kitchen = new Category();
	private Product flashlight = new Product();
	private Product kitchenRobot = new Product();
	private Product plate = new Product();

	@BeforeClass
	public void setUp() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		electro.setName("Electro");
		em.persist(electro);
		kitchen.setName("Kitchen");
		em.persist(kitchen);
		flashlight.setName("Flashlight");
		flashlight.addCategory(electro);
		em.persist(flashlight);
		kitchenRobot.setName("Kitchen Robot");
		kitchenRobot.addCategory(electro);
		kitchenRobot.addCategory(kitchen);
		em.persist(kitchenRobot);
		plate.setName("Plate");
		plate.addCategory(kitchen);
		em.persist(plate);

		em.getTransaction().commit();
		em.close();
	}

	@Test(expectedExceptions=ConstraintViolationException.class)
	public void testDoesntSaveNullName(){
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		Product phone = new Product();
		phone.setName(null);
		phone.addCategory(electro);
		em.persist(phone);

		em.getTransaction().commit();
		em.close();
	}

	@Test
	public void electroTest() {
		EntityManager em = emf.createEntityManager();

		Category category = em.find(Category.class, electro.getId());
		assertEquals(category.getProducts().size(), 2);
		assertContainsProductWithName(category.getProducts(), "Kitchen Robot");
		assertContainsProductWithName(category.getProducts(), "Flashlight");

		em.close();
	}

	@Test
	public void kitchenTest() {
		EntityManager em = emf.createEntityManager();

		Category category = em.find(Category.class, kitchen.getId());
		assertEquals(category.getProducts().size(), 2);
		assertContainsProductWithName(category.getProducts(), "Kitchen Robot");
		assertContainsProductWithName(category.getProducts(), "Plate");

		em.close();
	}

	@Test
	public void flashlightTest() {
		EntityManager em = emf.createEntityManager();

		Product product = em.find(Product.class, flashlight.getId());
		assertEquals(product.getCategories().size(), 1);
		assertContainsCategoryWithName(product.getCategories(), "Electro");

		em.close();
	}

	@Test
	public void kitchenRobotTest() {
		EntityManager em = emf.createEntityManager();

		Product product = em.find(Product.class, kitchenRobot.getId());
		assertEquals(product.getCategories().size(), 2);
		assertContainsCategoryWithName(product.getCategories(), "Electro");
		assertContainsCategoryWithName(product.getCategories(), "Kitchen");

		em.close();
	}

	@Test
	public void plateTest() {
		EntityManager em = emf.createEntityManager();

		Product product = em.find(Product.class, plate.getId());
		assertEquals(product.getCategories().size(), 1);
		assertContainsCategoryWithName(product.getCategories(), "Kitchen");

		em.close();
	}

	private void assertContainsCategoryWithName(Set<Category> categories,
			String expectedCategoryName) {
		for(Category cat: categories){
			if (cat.getName().equals(expectedCategoryName))
				return;
		}
			
		Assert.fail("Couldn't find category "+ expectedCategoryName+ " in collection "+categories);
	}
	private void assertContainsProductWithName(Set<Product> products,
			String expectedProductName) {
		
		for(Product prod: products){
			if (prod.getName().equals(expectedProductName))
				return;
		}
			
		Assert.fail("Couldn't find product "+ expectedProductName+ " in collection "+products);
	}

	
}
