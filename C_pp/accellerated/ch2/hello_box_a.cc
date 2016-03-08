#include <iostream>

using std::cout;
using std::cin;
using std::endl;
using std::string;

/* This is a variant on our boxed greeting from Chapter 2 with a configurable
 * greeting. The code is much simpler than the variant from the book, which we
 * adopt in hello_box_b.cc
 */
int main() {

  int padding;
  int i;

  string name;
  cout << "What is your name? ";
  cin >> name;
  cout << "How much padding would you like? ";
  cin >> padding;

  string padding_in_line = string(padding, ' ');
  string prefix = "*" + padding_in_line;
  string suffix = padding_in_line + "*";
  string greeting = "hello, " + name;
  string greeting_line = prefix + greeting + suffix;
  string spaces = string(greeting.size(), ' ');
  string line_of_spaces = prefix + spaces + suffix;
  string line_of_stars = string(greeting_line.size(), '*');

  cout << line_of_stars << '\n';
  for (i = 0; i < padding; i++) {
    cout << line_of_spaces << '\n';
  }
  cout << greeting_line << '\n';
  for (i = 0; i < padding; i++) {
    cout << line_of_spaces << '\n';
  }
  cout << line_of_stars << endl;
}
