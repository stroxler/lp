#include <iostream>
#include <string> // note: I forgot this earlier, it seems clang handles it...
#include <iomanip>
#include <vector>
#include <algorithm>

using std::string;
using std::cin;
using std::cout;
using std::endl;
using std::vector;


// NOTICE THE TYPEDEF!
typedef vector<double>::size_type vec_sz;

/* Second version of the grades program. It is very similar to the first
 * version, but uses a median, which in turn necessitates using a vector
 * and the sorting algorithm in the stl.
 *
 * Notice that unlike in (say) java, we don't have to call a constructor
 * here - in C++, a declaration like homework_grades makes an object, not
 * a reference. So we are implicitly calling the default constructor.
 *
 * If we used a reference or a pointer, we'd need to do something like new
 * vector(), since then the semantics would be more like java.
 */
int main() {
  double midterm_grade;
  double final_grade;
  vector<double> homework_grades;
  double homework_grade;
  double homework_median;
  vec_sz num_homeworks;
  double course_grade;

  cout << "Enter your midterm grade: ";
  cin >> midterm_grade;
  cout << "Enter your final grade: ";
  cin >> final_grade;
  cout << "Enter your homework grades, followed by EOF: ";
  while (cin >> homework_grade) {
    homework_grades.push_back(homework_grade);
  }

  num_homeworks = homework_grades.size();
  if (num_homeworks == 0) {
    cout << "You must enter at least one homework" << endl;
    return 1;
  }
  sort(homework_grades.begin(), homework_grades.end());
  vec_sz mid = num_homeworks / 2;
  homework_median =
    num_homeworks % 2 == 0 ?
    0.5 * (homework_grades[mid] + homework_grades[mid - 1]) :
    homework_grades[mid];

  course_grade =
    0.4 * homework_median + 0.4 * final_grade + 0.2 * midterm_grade;

  std::streamsize prec = cout.precision();
  cout << "\nYour course grade is: "
       << std::setprecision(4)
       << course_grade
       << std::setprecision(prec)
       << endl;
}
