package com.beitech.test.app.controllers;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.beitech.test.app.models.entity.Customer;
import com.beitech.test.app.models.service.ICustomerService;

/**
 * Clase controlador para Clientes
 * 
 * @author Juan Camilo Villamizar
 *
 */
@Controller
@SessionAttributes("customer")
public class CustomerController {

	@Autowired
	private ICustomerService customerService;

	/**
	 * Metodo para listar los clientes en el sistema
	 * 
	 * @param model objeto para enviar parametros a la vista
	 * @return pagina con el listado de clientes
	 */
	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(Model model) {

		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", customerService.findAll());
		return "listar";
	}

	/**
	 * Metodo para ver el detalle del cliente
	 * 
	 * @param id    Identificador del cliente
	 * @param model Model para enviar parametros a la pagina
	 * @param flash enviar mensajes a la pagina
	 * @return pagina con detalle del cliente
	 */
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Optional<Customer> clienteConsulta = customerService.findOne(id);
		Customer cliente = new Customer();
		if (clienteConsulta == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		} else {
			cliente = clienteConsulta.get();
		}

		model.put("customer", cliente);
		model.put("titulo", "Detalle cliente: " + cliente.getNombre());
		return "ver";
	}

}
