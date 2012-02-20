package pl.squirrel.svt;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloWorldController {

	@RequestMapping("/hello.html")
	public String velo(ModelMap model) {
		return "hello_velocity";
	}
}
