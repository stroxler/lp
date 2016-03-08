#include <iostream>
#include <string> // note: I forgot this earlier, it seems clang handles it...
#include <iomanip>

using std::string;
using std::cin;
using std::cout;
using std::endl;


/* Initial grades program. We make no use of the stl here, because we can
 * compute the homework mean as a running average. The grades_vector
 * variant uses the stl because it computes a median, which requires storing
 * the grades.
 *
 * Using setprecision sets the number of significant digits we want (not
 * the number of decimal places)
 *
 * Note that we save the initial precision and reset cout to use it. This is
 * considered good practice, since other libraries can be relying on the
 * previously-set state.
 */
int main() {
  double midterm_grade;
  double final_grade;
  double homework_grade;
  double homework_average;
  double homework_sum;
  double homework_count;
  double course_grade;

  cout << "Enter your midterm grade: ";
  cin >> midterm_grade;
  cout << "Enter your final grade: ";
  cin >> final_grade;
  cout << "Enter your homework grades, followed by EOF: ";
  while (cin >> homework_grade) {
    homework_count += 1;
    homework_sum += homework_grade;
  }

  homework_average = homework_sum / homework_count;
  course_grade =
    0.4 * homework_average + 0.4 * final_grade + 0.2 * midterm_grade;

  std::streamsize prec = cout.precision();
  cout << "\nYour course grade is: "
       << std::setprecision(4)
       << course_grade
       << std::setprecision(prec)
       << endl;
}
