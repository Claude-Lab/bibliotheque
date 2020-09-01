/**
 * 
 */
package fr.lusseau.bibliotheque.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import fr.lusseau.bibliotheque.entity.Caution;
import fr.lusseau.bibliotheque.entity.Coordonnee;
import fr.lusseau.bibliotheque.entity.Emprunt;
import fr.lusseau.bibliotheque.entity.Personne;
import fr.lusseau.bibliotheque.entity.Role;
import fr.lusseau.bibliotheque.service.GestionCaution;
import fr.lusseau.bibliotheque.service.GestionCoordonnee;
import fr.lusseau.bibliotheque.service.GestionEmprunt;
import fr.lusseau.bibliotheque.service.GestionPersonne;
import fr.lusseau.bibliotheque.service.GestionRole;

/**
 * Classe en charge de la gestion et de la synchronisation des événements liés à
 * la classe Personne.
 * 
 * @Version Bibliotheque -v1,0
 * @date 14 août 2020 - 14:50:43
 * @author Claude LUSSEAU
 *
 */
@Controller
public class PersonneController {

	@Autowired
	GestionPersonne gp;
	@Autowired
	GestionRole gr;
	@Autowired
	GestionCaution gc;
	@Autowired
	GestionCoordonnee ga;
	@Autowired
	GestionEmprunt ge;

	@PostConstruct
	private void init() {
	}
	

	@RequestMapping(path = "/listePersonnes", method = RequestMethod.GET)
	public ModelAndView listerPersonnes() {
		List<Personne> listeP = gp.listePersonnes();
		return new ModelAndView("/admin/listes/listePersonnes", "listeP", listeP);
	}

	@RequestMapping(path = "/gestionPersonnes", method = RequestMethod.GET)
	public ModelAndView gererPersonnes() {
		List<Personne> listeP = gp.listePersonnes();
		Personne pers = new Personne();
		ModelAndView mav = new ModelAndView("/admin/gestion/gestionPersonnes", "listeP", listeP);
		mav.getModelMap().addAttribute("pers", pers);
		return mav;
	}

	@RequestMapping(value = "/ajoutPersonne", method = RequestMethod.GET)
	public ModelAndView ajouterPersonne() {
		Personne pers = new Personne();
		List<Role> listeRoles = gr.listeRoles();
		List<Caution> listeCautions = gc.listeCautions();
		ModelAndView mav = new ModelAndView("/admin/ajouts/ajoutPersonne", "pers", pers);
		mav.getModelMap().addAttribute("listeRoles", listeRoles);
		mav.getModelMap().addAttribute("listeCautions", listeCautions);
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/validPersonne")
	public String ajoutPersonneValid(@Valid @ModelAttribute("pers, coordonnee, caution, role") Personne pers,
			Coordonnee coordonnee, BindingResult result) {
		if (result.hasErrors()) {
			return "/admin/ajouts/ajoutPersonne";
		} else {
			gp.ajouterPersonne(pers);
			return "redirect:/gestionPersonnes";
		}
	}

	@RequestMapping(value = "/detailsPersonne", method = RequestMethod.GET)
	public ModelAndView detailsPersonne(String index) {
		int i = Integer.parseInt(index.substring(1));
		Personne pers;
		Role role = null;
		Caution caution = null;
		Coordonnee coordonnee = null;
		List<Emprunt> emprunts = null;
		pers = gp.trouverPersonne(i);
		
		ModelAndView mav = new ModelAndView("/admin/details/detailsPersonne", "pers", pers);
		mav.getModelMap().addAttribute(index, role);
		mav.getModelMap().addAttribute(index, caution);
		mav.getModelMap().addAttribute(index, coordonnee);
		mav.getModelMap().addAttribute(index, emprunts);
		
		return mav;

	}

	@RequestMapping(value = "/modifierPersonne", method = RequestMethod.GET)
	public ModelAndView modifPersonne(String index) {
		int i = Integer.parseInt(index.substring(1));
		List<Role> listeRoles = gr.listeRoles();
		List<Caution> listeCautions = gc.listeCautions();
		ModelAndView mav = new ModelAndView("/admin/modifs/modifPersonne", "pers", gp.trouverPersonne(i));
		mav.getModelMap().addAttribute("listeRoles", listeRoles);
		mav.getModelMap().addAttribute("listeCautions", listeCautions);
		return mav;
	}

	@RequestMapping(value = "/modifierPersonneValid", method = RequestMethod.POST)
	public ModelAndView listePersonneValid(Personne pers) {
		gp.modifierPersonne(pers);
		return gererPersonnes();
	}

	@RequestMapping(value = "/supprimerPersonne", method = RequestMethod.GET)
	public ModelAndView supprimerPersonne(String index) {
		int i = Integer.parseInt(index.substring(1));
		Personne personne = gp.trouverPersonne(i);
		try {
			gp.supprimerPersonne(personne);
		} catch (Exception e) {
		}

		return gererPersonnes();
	}

}
