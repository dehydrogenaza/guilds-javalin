import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class EmployeeDao {
  private static final Map<Integer, Employee> EMPLOYEES = new ConcurrentHashMap<>();
  private static final AtomicInteger ID_GENERATOR = new AtomicInteger(5);

  static {
    EMPLOYEES.put(1, new Employee("Bob Budowniczy", "bob@fachowiec.com", 8000));
    EMPLOYEES.put(2, new Employee("Listonosz Pat", "pat@poczta.com", 4500));
    EMPLOYEES.put(3, new Employee("Kaczor Donald", "donald@mckwacz.com", 5000));
    EMPLOYEES.put(4, new Employee("Krtek", "krtek@fachowiec.cz", 10000));
    EMPLOYEES.put(5, new Employee("Kulfon", "kulfon@ciuchcia.pl", 25000));
  }

  public Optional<Employee> findById(int id) {
    var employee = EMPLOYEES.get(id);
    return Optional.ofNullable(employee);
  }

  public List<Employee> findAll() {
    return EMPLOYEES.values()
        .stream()
        .toList();
  }

  public void save(Employee employee) {
    var id = ID_GENERATOR.incrementAndGet();
    EMPLOYEES.put(id, employee);
  }

  public void delete(int id) {
    EMPLOYEES.remove(id);
  }
}
