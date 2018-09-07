package com.beitech.test.app.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.beitech.test.app.models.entity.Customer;
import com.beitech.test.app.models.entity.Order;
import com.beitech.test.app.models.entity.OrderDetail;
import com.beitech.test.app.models.entity.Product;
import com.beitech.test.app.models.service.ICustomerService;

/**
 * Controlador encargado para operaciones de Ordenes
 * 
 * @author Juan Camilo Villamizar
 *
 */
@Controller
@RequestMapping("/order")
@SessionAttributes("order")
public class OrderController {

	@Autowired
	private ICustomerService customerService;

	/**
	 * Metodo para crear una orden
	 * 
	 * @param clienteId Identificador del cliente
	 * @param model     Modelo para enviar parametros a la pagina
	 * @param flash     Envia mensajes a la pagina
	 * @return Pagina de creacion
	 */
	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable(value = "clienteId") Long clienteId, Map<String, Object> model,
			RedirectAttributes flash) {

		Optional<Customer> customer = customerService.findOne(clienteId);

		if (customer == null) {
			flash.addFlashAttribute("error", "El cliente no se encuentra en la base de datos");
			return "redirect:/listar";
		}

		Order order = new Order();
		order.setCustomer(customer.get());

		model.put("order", order);
		model.put("titulo", "Crear Orden");

		return "order/form";
	}

	/**
	 * Cargar productos
	 * 
	 * @param term autocompletar para peticion ajax
	 * @return listado de productos
	 */
	@GetMapping(value = "/cargar-productos/{term}", produces = { "application/json" })
	public @ResponseBody List<Product> cargarProductos(@PathVariable String term) {
		return customerService.findByName(term);
	}

	/**
	 * Metodo para guardar orden
	 * 
	 * @param order    Objeto orden
	 * @param result   Validacion de formulario
	 * @param model    Envio de parametros ala pagina
	 * @param itemId   cantidad de items
	 * @param cantidad cantidad de items seleccionados
	 * @param flash    envia mensajes a la pagina
	 * @param status   estado de sesion
	 * @return
	 */
	@PostMapping("/form")
	public String guardar(@Valid Order order, BindingResult result, Model model,
			@RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidad, RedirectAttributes flash,
			SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear Factura");
			return "order/form";
		}

		if (itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", "Crear Factura");
			model.addAttribute("error", "La factura debe tener items");
			return "order/form";
		}

		if (cantidad.length > 5) {
			flash.addFlashAttribute("error", "No puede agregar más de 5 productos");
			return "redirect:/listar";
		}

		for (int i = 0; i < itemId.length; i++) {
			Optional<Product> product = customerService.findProductById(itemId[i]);
			OrderDetail detail = new OrderDetail();
			detail.setPrice((Long) cantidad[i].longValue());
			detail.setProduct(product.get());

			order.addOrderDetail(detail);
		}

		customerService.saveOrder(order);
		status.setComplete();
		flash.addFlashAttribute("success", "Orden creada con exito!");

		return "redirect:/ver/" + order.getCustomer().getId();
	}

	/**
	 * metodo para retornar informacion completa de factura
	 * 
	 * @param id    Identificador de la factura
	 * @param model envia parametros a la pagina
	 * @param flash envia mensajes a la pagina
	 * @return pagina con informacion de la orden
	 */
	@GetMapping("ver/{id}")
	public String ver(@PathVariable(name = "id") Long id, Model model, RedirectAttributes flash) {

		Optional<Order> order = customerService.findOrderById(id);

		if (order == null) {
			flash.addFlashAttribute("error", "No existe la orden en la base de datos");
			return "redirect:/listar";
		}

		model.addAttribute("order", order.get());
		model.addAttribute("titulo", "Oden: ".concat(order.get().getDeliveryAddress()));

		return "order/ver";
	}

}
