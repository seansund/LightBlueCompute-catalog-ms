package catalog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import catalog.models.Inventory;
import catalog.models.InventoryRepo;

/**
 * REST Controller to manage Inventory database
 */
@RestController
public class CatalogController {

	Logger logger = LoggerFactory.getLogger(CatalogController.class);

	@Autowired
	InventoryRepo itemsRepo;

	/**
	 * @return all items in inventory
	 */
	@RequestMapping(value = "/items", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	ResponseEntity<Iterable<Inventory>> getInventory() {
		Iterable<Inventory> inventory = itemsRepo.findAll();
		
		return ResponseEntity.ok(inventory);
	}

	/**
	 * @return item by id
	 */
	@RequestMapping(value = "/items/{id}", method = RequestMethod.GET)
	ResponseEntity<?> getById(@PathVariable long id) {
		if (!itemsRepo.exists(id)) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(itemsRepo.findOne(id));
	}

}
