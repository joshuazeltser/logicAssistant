package model;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PropositionalController {

	    @GetMapping("/")
		public String expressionForm(Model model) {
			model.addAttribute("proof", new Proof());
			return "propositional";
		}

		@PostMapping("/")
		public String expressionSubmit(@ModelAttribute Proof proof) {
	    	return "propositional";
		}

}
