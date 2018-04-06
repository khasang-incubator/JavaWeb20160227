package io.khasang.enterprise.service;

import io.khasang.enterprise.dao.interfaces.ClientDao;
import io.khasang.enterprise.dao.interfaces.EmployeeDao;
import io.khasang.enterprise.dao.interfaces.ProjectDao;
import io.khasang.enterprise.dao.interfaces.TrackDao;
import io.khasang.enterprise.model.Client;
import io.khasang.enterprise.model.Employee;
import io.khasang.enterprise.model.Project;
import io.khasang.enterprise.model.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("adminService")
@Transactional
public class AdminService {
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private TrackDao trackDao;
    @Autowired
    private ProjectDao projectDao;

    /**
     * Client service
     */

    @Transactional
    public void addClientRole(int id) {
        clientDao.addClientRole(id);
    }

    @Transactional
    public void deleteClientById(int id) {
        deleteClientRoleById(id);
        clientDao.deleteClientById(id);
    }

    @Transactional
    private void deleteClientRoleById(int id) {
        clientDao.deleteClientRoleById(id);
    }

    @Transactional
    public void deleteAllClients() {
        deleteAllClientRoles();
        clientDao.deleteAllClients();
    }

    @Transactional
    private void deleteAllClientRoles() {
        clientDao.deleteAllClientRoles();
    }

    @Transactional
    public List<Client> getAllClients() {
        return clientDao.findAll();
    }

    @Transactional
    public Client getClientByLogin(String login) {
        return clientDao.findByLogin(login);
    }

    @Transactional
    public void banClient(String login) {
        Client client = clientDao.findByLogin(login);
        client.setEnabled(false);
        clientDao.update(client);
    }

    @Transactional
    public void unbanClient(String login) {
        Client client = clientDao.findByLogin(login);
        client.setEnabled(true);
        clientDao.update(client);
    }

    /**
     * Employee service
     */
    @Transactional
    public void addEmployeeRole(int id) {
        employeeDao.addEmployeeRole(id);
    }

    @Transactional
    public void deleteEmployeeById(int id) {
        deleteEmployeeRoleById(id);
        employeeDao.deleteEmployeeById(id);
    }

    @Transactional
    private void deleteEmployeeRoleById(int id) {
        employeeDao.deleteEmployeeRoleById(id);
    }

    @Transactional
    public void deleteAllEmployees() {
        deleteAllEmployeesRoles();
        employeeDao.deleteAllEmployees();
    }

    @Transactional
    private void deleteAllEmployeesRoles() {
        employeeDao.deleteAllEmployeesRoles();
    }

    @Transactional
    public List<Employee> getAllEmployees() {
        return employeeDao.findAll();
    }

    @Transactional
    public Employee getEmployeeByLogin(String login) {
        return employeeDao.findByLogin(login);
    }

    @Transactional
    public String banEmployee(String login) {
        Employee employee = employeeDao.findByLogin(login);
        if (!isAdmin(employee)) {
            employee.setEnabled(false);
            employee.setFireDate(new Date());
            employeeDao.update(employee);
            return "redirect:/admin/all_employee";
        } else {
            return "redirect:/admin/error300";
        }
    }

    @Transactional
    public void unbanEmployee(String login) {
        Employee employee = employeeDao.findByLogin(login);
        employee.setEnabled(true);
        employee.setFireDate(null);
        employeeDao.update(employee);
    }

    private boolean isAdmin(Employee employee) {
        if (employee.getId() == 1) return true;
        else return false;
    }

    @Transactional
    public List<Track> getEmployeeTracks(Integer id) {
        return trackDao.findAllByEmployeeId(id);
    }

    /**
     * Project service
     */

    @Transactional
    public List<Project> getUnfinishedProjects() {
        return projectDao.findUnfinishedProjects();
    }

    @Transactional
    public List<Project> getFinishedProjects() {
        return projectDao.findFinishedProjects();
    }
}