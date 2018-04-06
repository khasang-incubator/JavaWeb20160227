package io.khasang.enterprise.controller.admin;

import io.khasang.enterprise.dao.interfaces.ClientDao;
import io.khasang.enterprise.dao.interfaces.EmployeeDao;
import io.khasang.enterprise.model.*;
import io.khasang.enterprise.model.enums.TrackStatus;
import io.khasang.enterprise.service.AdminService;
import io.khasang.enterprise.service.ProjectTrackingService;
import io.khasang.enterprise.service.registrationService.EmployeeValidator;
import io.khasang.enterprise.service.registrationService.RegistrationService;
import io.khasang.enterprise.webservice.exchangerates.CurrencyService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private EmployeeValidator employeeValidator;
    @Autowired
    RegistrationService registrationService;
    @Autowired
    private ProjectTrackingService projectTrackingService;

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String adminHome(Model model) {
        model.addAttribute("USD", currencyService.getCurrency("USD"));
        model.addAttribute("EUR", currencyService.getCurrency("EUR"));
        model.addAttribute("currentDay", currencyService.getCurrentDay());
        return "admin/account";
    }

    /**
     * CRUD operations for Client
     */

    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    public String adminGetAllClient() {
        return "admin/clients";
    }

    @Transactional
    @RequestMapping(value = "/client/{login}", method = RequestMethod.GET)
    public String adminGetCurrentClient(@PathVariable("login") String login, Model model) {
        Client client = adminService.getClientByLogin(login);
        Set<ClientRole> roles = client.getClientRoles();
        Set<Project> projects = client.getProjects();
        Hibernate.initialize(roles);
        model.addAttribute("roles", roles);
        Hibernate.initialize(projects);
        model.addAttribute("projects", projects);
        model.addAttribute("client", client);
        return "admin/client";
    }

    @Transactional
    @RequestMapping(value = "/client/{login}", method = RequestMethod.POST)
    public ModelAndView clientFinder(@RequestParam("login") String login, ModelMap model) {
        if(clientDao.findByLogin(login) == null) {
            model.addAttribute("error", new Exception("Клиент не найден в Базе"));
            return new ModelAndView("admin/find_client", model);
        }
        Client client = adminService.getClientByLogin(login);
        Set<ClientRole> roles = client.getClientRoles();
        Hibernate.initialize(roles);
        model.addAttribute("roles", roles);
        model.addAttribute("client", client);
        return new ModelAndView("admin/client", model);
    }

    @RequestMapping(value = "/find_client", method = RequestMethod.GET)
    public String findClient() {
        return "admin/find_client";
    }

    @RequestMapping(value = "/client/ban/{login}", method = RequestMethod.GET)
    public String disableClient(@PathVariable("login") String login) {
        adminService.banClient(login);
        return "redirect:/admin/all_clients";
    }

    @RequestMapping(value = "/client/unban/{login}", method = RequestMethod.GET)
    public String enableClient(@PathVariable("login") String login) {
        adminService.unbanClient(login);
        return "redirect:/admin/all_clients";
    }

    @RequestMapping(value = "/all_clients", method = RequestMethod.GET)
    public String adminClients(Model model) {
        model.addAttribute("clients", adminService.getAllClients());
        return "admin/all_clients";
    }

    /**
     * CRUD operations for Employee
     */

    @RequestMapping(value = "/organization", method = RequestMethod.GET)
    public String adminOrganization() {
        return "admin/organization";
    }

    @RequestMapping(value = "/all_employee", method = RequestMethod.GET)
    public String getAllEmployee(Model model) {
        model.addAttribute("employee", adminService.getAllEmployees());
        return "admin/all_employees";
    }

    @Transactional
    @RequestMapping(value = "/employee/{login}", method = RequestMethod.GET)
    public String getCurrentEmployee(@PathVariable("login") String login, Model model) {
        Employee employee = adminService.getEmployeeByLogin(login);
        Set<EmployeeRole> roles = employee.getEmployeeRoles();
        Hibernate.initialize(roles);
        model.addAttribute("roles", roles);
        model.addAttribute("employee", employee);
        return "admin/employee";
    }

    @Transactional
    @RequestMapping(value = "/employee/{login}", method = RequestMethod.POST)
    public ModelAndView employeeFinder(@RequestParam("login") String login, ModelMap model) {
        if(employeeDao.findByLogin(login) == null) {
            model.addAttribute("error", new Exception("Сотрудник не найден в Базе"));
            return new ModelAndView("admin/find_employee", model);
        }
        Employee employee = adminService.getEmployeeByLogin(login);
        Set<EmployeeRole> roles = employee.getEmployeeRoles();
        Hibernate.initialize(roles);
        model.addAttribute("roles", roles);
        model.addAttribute("employee", employee);
        return new ModelAndView("admin/employee", model);
    }

    @RequestMapping(value = "/find_employee", method = RequestMethod.GET)
    public String findEmployee() {
        return "admin/find_employee";
    }

    @RequestMapping(value = "/employee/ban/{login}", method = RequestMethod.GET)
    public String disableEmployee(@PathVariable("login") String login) {
        return adminService.banEmployee(login);
    }

    @RequestMapping(value = "/employee/unban/{login}", method = RequestMethod.GET)
    public String enableEmployee(@PathVariable("login") String login) {
        adminService.unbanEmployee(login);
        return "redirect:/admin/all_employee";
    }

    @RequestMapping(value = "/error300", method = RequestMethod.GET)
    public String accessDenied() {
        return "admin/300";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String adminRegistration(Model model) {
        model.addAttribute("employee", new Employee());
        return "admin/registration";
    }

    @RequestMapping(value = "/registration/new_employee", method = RequestMethod.POST)
    public String saveEmployee(@ModelAttribute("employee")@Valid Employee employee, BindingResult result, Model model) {
            if(result.hasErrors()) {
            return "admin/registration";
        } else {
            employee.setEnabled(true);
            registrationService.saveEmployeeinDB(employee);
            model.addAttribute("employee", employee);
            return "admin/organization";
        }
    }
    @InitBinder("employee")
    public void initClientBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(employeeValidator);
    }

    @Transactional
    @RequestMapping(value = "/employee/{login}/tracks", method = RequestMethod.GET)
    public String getEmployeeProjects(@PathVariable("login") String login, Model model) {
        Employee employee = adminService.getEmployeeByLogin(login);
        List<Track> tracks = adminService.getEmployeeTracks(employee.getId());
        Hibernate.initialize(tracks);
        model.addAttribute("runningTracks", getRunningTracks(tracks));
        model.addAttribute("confirmedTracks", getConfirmedTracks(tracks));
        model.addAttribute("employee", employee);
        return "admin/employee_tracks";
    }

    private List<Track> getRunningTracks(List<Track> tracks) {
        List<Track> result = new ArrayList<>();
        for (Track track: tracks) {
            if (track.getTrackStatus().equals(TrackStatus.RUNNING)) {
                result.add(track);
            }
        }
        return result;
    }

    private List<Track> getConfirmedTracks(List<Track> tracks) {
        List<Track> result = new ArrayList<>();
        for (Track track: tracks) {
            if (track.getTrackStatus().equals(TrackStatus.CONFIRMED)) {
                result.add(track);
            }
        }
        return result;
    }

    /**
     * CRUD operations for Projects
     */

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public String adminProjects() {
        return "admin/projects";
    }

    @RequestMapping(value = "/all_projects", method = RequestMethod.GET)
    public String allProjects(Model model) {
        List<Project> openProjects = adminService.getUnfinishedProjects();
        List<Project> finishedProjects = adminService.getFinishedProjects();
        model.addAttribute("openProjects", openProjects);
        model.addAttribute("finishedProjects", finishedProjects);
        return "admin/all_projects";
    }

    @RequestMapping(value = "/projects/{id}", method = RequestMethod.GET)
    public String adminProject(@PathVariable("id") Integer id, Model model) {
        Project project = projectTrackingService.getProjectById(id);
        model.addAttribute("project", project);
        return "admin/project";
    }

    @RequestMapping(value = "/projects/{projectId}/orders", method = RequestMethod.GET)
    public String adminGetProjectOrders(@PathVariable("projectId") Integer projectId, Model model) {
        projectTrackingService.getProjectOrders(projectId);
        model.addAttribute("ordersOfProject", projectTrackingService.getProjectOrders(projectId));
        model.addAttribute("project", projectTrackingService.getProjectById(projectId));
        return "admin/projectOrders";
    }

    @RequestMapping(value = "/project/{projectId}/order/{orderId}/tracks", method = RequestMethod.GET)
    public String getTrackingHistoryOfOrder(@PathVariable("projectId") Integer projectId,
                                            @PathVariable("orderId") Integer orderId, Model model) {

        model.addAttribute("orderOfProject", projectTrackingService.getOrderById(orderId));
        model.addAttribute("trackingProject", projectTrackingService.getProjectById(projectId));
        model.addAttribute("allTracks", projectTrackingService.getTrackingHistoryOfOrder(orderId));
        return "admin/tracks";
    }

    @RequestMapping(value = "/project/find", method = RequestMethod.GET)
    public String findProject() {
        return "admin/find_project";
    }

    @Transactional
    @RequestMapping(value = "/project/find", method = RequestMethod.POST)
    public ModelAndView projectFinder(@RequestParam("title") String title, ModelMap model) {
        if(projectTrackingService.getProjectByTitle(title) == null) {
            model.addAttribute("error", new Exception("Проект не найден в Базе"));
            return new ModelAndView("admin/find_project", model);
        }
        Project project = projectTrackingService.getProjectByTitle(title);
        model.addAttribute("project", project);
        return new ModelAndView("admin/project", model);
    }

    @RequestMapping(value = "/news", method = RequestMethod.GET)
    public String adminNews() {
        return "admin/news";
    }

    @RequestMapping(value = "/finance", method = RequestMethod.GET)
    public String adminFinance() {
        return "admin/finance";
    }

    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    public String adminChat() {
        return "admin/chat";
    }
}