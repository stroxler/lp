#include <iostream>

using std::cout;
using std::cin;
using std::endl;
using std::string;

/* This solution is still closer to hello_box_a than it is to the book
 * solution (personally I find the book solution too strange to get without
 * copying it; the logic seems convoluted to me). But it's a little more like
 * the book, and demonstrates a few ideas:
 *   - using string::size_type in a program, which is the correct way to 
 *     work with string sizes (int will work, but isn't so proper and could
 *     involve coercion)
 *       ... this kind of code is one reason why auto is considered a best
 *           practice in modern c++, but this book is about c++ 98, not newer
 *           stuff
 *   - slightly more involved logical operators.
 */
int main() {

  int padding;
  int r, c;

  string name;
  cout << "What is your name? ";
  cin >> name;
  cout << "How much padding would you like? ";
  cin >> padding;

  int num_rows = 2 * padding + 3;

  string greeting = "hello, " + name;

  string::size_type greeting_length = greeting.size();
  string::size_type inner_line_length = greeting_length + 2 * padding;
  string::size_type outer_line_length = inner_line_length + 2;

  string in_line_padding = string(padding, ' ');
  string prefix = "*" + in_line_padding;
  string suffix = in_line_padding + "*";
  string padding_line = prefix + string(greeting_length, ' ') + suffix;
  string greeting_line = prefix + greeting + suffix;
  string stars_line = string(outer_line_length, '*');

  for (r = 0; r < num_rows; r++) {
    if (r == 0 || r == num_rows -1) {
      cout << stars_line << '\n';
    } else if (r == padding + 1) {
      cout << greeting_line << '\n';
    } else {
      cout << padding_line << '\n';
    }
  }
  cout << endl;
}
