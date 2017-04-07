package model;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ValidityController {

	    @GetMapping("/validity")
		public String expressionForm(Model model) {
			model.addAttribute("truthTable", new TruthTable());
			return "validity";
		}

		@PostMapping("/validity")
		public String expressionSubmit(@ModelAttribute TruthTable truthTable) {
	    	return "validity";
		}

}
