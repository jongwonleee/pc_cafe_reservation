#include "Link_DB.h"

void Link_DB::connDB()
{
	conn = mysql_init(NULL);	//Visual studio�� Workbench�� ����

	if (!mysql_real_connect(conn, server, user, password, database, port, NULL, 0))
	{
		fprintf(stderr, "%s ", mysql_error(conn));			//����ó���� (������ ���� �ʾ������ �����޼��� ��� �� ����)
		exit(1);
	}
}

void Link_DB::disconnDB()
{
	mysql_close(conn);	//VIsual studio�� Workbench ���� ����
}

void Link_DB::inputData()
{
	cin >> ID >> PW >> Name >> Email >> Phonenum;
}

void Link_DB::showData() 
{
	res = mysql_use_result(conn);

	int num_fields = mysql_num_fields(res);
	//��� ���� Workbench���� ������
	while ((row = mysql_fetch_row(res)))
	{
		for (int i = 0; i < num_fields; i++)
		{
			if (row[i] != NULL)
				cout << row[i] << endl;				//��� column ���
			else
				cout << "NULL" << endl;
		}
	}

}

void Link_DB::sendInsertQuery()
{
	str_query = "INSERT INTO custom (ID, PW, Name, email, phonenum) VALUES (\"";	//INSERT query��
	str_query = str_query + ID + "\", \"" + PW + "\", \"" + Name + "\", \"" + Email + "\", \"" + Phonenum + "\");";

	mysql_query(conn, str_query.c_str());	//Workbench�� query�� ����
}

void Link_DB::sendSelectQuery()
{
	str_query = "SELECT * FROM custom";	//SELECT query��
	mysql_query(conn, str_query.c_str());	//Workbench�� query�� ����
}