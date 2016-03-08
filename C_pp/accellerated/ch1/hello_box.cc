#include <iostream>

using std::cout;
using std::cin;
using std::endl;
using std::string;

int main() {
  string name;
  cout << "What is your name? ";
  cin >> name;

  string prefix = "* ";
  string suffix = " *";
  string greeting = "hello, " + name;
  string greeting_line = prefix + greeting + suffix;
  string spaces = string(greeting.size(), ' ');
  string line_of_spaces = prefix + spaces + suffix;
  string line_of_stars = string(greeting_line.size(), '*');

  cout << line_of_stars << "\n";
  cout << line_of_spaces << "\n";
  cout << greeting_line << "\n";
  cout << line_of_spaces << "\n";
  cout << line_of_stars << "\n";
}
