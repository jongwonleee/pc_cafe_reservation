#include "Link_DB.h"

int len(string*input)
{
	int count = 0;
	while (input[count] != "\0") count++;
	return count;
}

int main()
{
	Link_DB db;
	string* res;
	res = db.sendQuery("select * from owner");
	cout << len(res);
	for (int i = 0; i < len(res); i++)
		cout << res[i] << endl;
}

