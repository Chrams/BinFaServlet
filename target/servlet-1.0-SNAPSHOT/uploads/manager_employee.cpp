#include <iostream>
#include <string>
#include <list>
#include <set>

class Employee{
  std::string first_name, family_name;
  short department;
  
public:
  Employee(const std::string& name, int dept) : family_name(name), department(dept)
  { }
  virtual void print() const;
  
};

void Employee::print() const
{
  std::cout << family_name << "\t" << department << std::endl;
}


class Manager : public Employee{
  //std::set<Employee*> group;
  short level;
public:
  Manager(const std::string& name, int dept, int lvl)
    :Employee(name,dept), level(lvl)
  { }
  void print() const;
};

void Manager::print() const
{
  Employee::print();
  std::cout << "\tlevel: " << level << std::endl;
}


void print_list(const std::list<Employee*>& s)
{
  for(std::list<Employee*>::const_iterator p = s.begin(); p!=s.end(); ++p)
  {
    (*p)->print();
  }
}

int main()
{
  Employee e("Brown",1234);
  Manager m("Smith",1234,2);
  std::list<Employee*> empl;
  empl.push_front(&e);
  empl.push_front(&m);
  print_list(empl);
  
  
  return 0;
}
