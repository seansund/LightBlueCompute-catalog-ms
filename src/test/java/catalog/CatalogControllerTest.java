package catalog;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import catalog.models.Inventory;
import catalog.models.InventoryRepo;

public class CatalogControllerTest {
	
	@Mock InventoryRepo itemsRepo;
	@InjectMocks CatalogController controller;
	
	private 	MockMvc mockMvc;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		
		mockMvc = MockMvcBuilders
			.standaloneSetup(controller)
			.build();
	}

	@Test
	public void canary() {
		assertThat(true, is(equalTo(true)));
	}
	
	@Test
	public void getInventory() throws Exception {
		final List<Inventory> inventory = new ArrayList<Inventory>();
		inventory.add(new Inventory(1));
		inventory.add(new Inventory(2));
		
		when(itemsRepo.findAll()).thenReturn(inventory);
		
		mockMvc.perform(get("/items"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id", is(equalTo(1))))
			.andExpect(jsonPath("$[1].id", is(equalTo(2))));
	}
	
	@Test
	public void getById_notFound() throws Exception {
		when(itemsRepo.exists(any())).thenReturn(false);
		
		mockMvc.perform(get("/items/1"))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void getById_success() throws Exception {
		final int id = 1;
		Inventory inventory = new Inventory(id);
		
		when(itemsRepo.exists(any())).thenReturn(true);
		when(itemsRepo.findOne(any())).thenReturn(inventory);
		
		mockMvc.perform(get("/items/" + id))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("$.id", is(equalTo(id))));
	}
}
