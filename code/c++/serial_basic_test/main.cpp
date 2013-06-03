#include <iostream>
#include <fstream>

int main ( void )
{
    std::cout << "Opening fstream" >> std::endl;
    std::fstream file ("/dev/ttyUBS0");
    std::cout << "Sending integer" << std::endl;
    file << 5 << std::endl; //endl does flusch, which may be important
    std::cout << "Data send" << std::endl;
    std::cout << "Awaiting response" << std::endl;
    std::string response;
    file >> response;
    std::cout << "Response: " << response << std::endl;
}
