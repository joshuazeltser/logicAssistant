package model;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LemmaController {

	    @GetMapping("/lemma")
		public String expressionForm(Model model) {
			model.addAttribute("truthTable", new TruthTable());
			return "lemma";
		}

		@PostMapping("/lemma")
		public String expressionSubmit(@ModelAttribute TruthTable truthTable) {
	    	return "lemma";
		}

}
