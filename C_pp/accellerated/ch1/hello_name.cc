#include <iostream>

/* Non-obvious notes:
 *  1. Remember this is C++, where there's a difference between an object
 *     and a reference. Name is an object. If we assign name2 = name, this is
 *     a copy operation, not a second reference to the same item!
 *  2. The cin >> operation splits on whitespace. If you give it your full
 *     name, it prints out only your first name.
 *  3. Any time we read cin, the system automatically flushes cout. This is
 *     important, since otherwise it might buffer and the user wouldn't see
 *     our input!
 *     ... also, std::endl flushes cout (that's the main difference between
 *     it and just using '\n')
 */
int main() {
  std::string name;
  std::cout << "What is your name?" << std::endl;
  std::cin >> name;
  std::cout << "hello, " << name << std::endl;
}
