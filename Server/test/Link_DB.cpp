#include "Link_DB.h"

void Link_DB::connDB()
{
	conn = mysql_init(NULL);	//Visual studio와 Workbench를 연결

	if (!mysql_real_connect(conn, server, user, password, database, port, NULL, 0))
	{
		fprintf(stderr, "%s ", mysql_error(conn));			//예외처리문 (연결이 되지 않았을경우 에러메세지 출력 및 종료)
		exit(1);
	}
}

void Link_DB::disconnDB()
{
	mysql_close(conn);	//VIsual studio와 Workbench 연결 해지
}

void Link_DB::inputData()
{
	cin >> ID >> PW >> Name >> Email >> Phonenum;
}

void Link_DB::showData() 
{
	res = mysql_use_result(conn);

	int num_fields = mysql_num_fields(res);
	//모든 행을 Workbench에서 가져옴
	while ((row = mysql_fetch_row(res)))
	{
		for (int i = 0; i < num_fields; i++)
		{
			if (row[i] != NULL)
				cout << row[i] << endl;				//모든 column 출력
			else
				cout << "NULL" << endl;
		}
	}

}

void Link_DB::sendInsertQuery()
{
	str_query = "INSERT INTO custom (ID, PW, Name, email, phonenum) VALUES (\"";	//INSERT query문
	str_query = str_query + ID + "\", \"" + PW + "\", \"" + Name + "\", \"" + Email + "\", \"" + Phonenum + "\");";

	mysql_query(conn, str_query.c_str());	//Workbench에 query문 전송
}

void Link_DB::sendSelectQuery()
{
	str_query = "SELECT * FROM custom";	//SELECT query문
	mysql_query(conn, str_query.c_str());	//Workbench에 query문 전송
}