#pragma comment (lib, "libmySQL.lib")
#pragma comment (lib, "ws2_32.lib")

#include <iostream>
#include <string>
#include <string.h>
#include <my_global.h>
#include <mysql.h>

using namespace std;
//��ü���� Ʋ�� ���۸��ؼ� �����ؿ�
class Link_DB
{
private:
	//�������� ���۸��� ���� �� ����
	MYSQL *conn;
	MYSQL_RES *res = NULL;
	MYSQL_ROW row;
	MYSQL_FIELD *fields;

	int len;
	//�� �κ��� DB�� �������ִ� �κ���
	char *server = "localhost";//������ ����ȣ��Ʈ����
	char *user = "root";//�����̸���, MySQL ���� �̸� �Է�
	char *password = "0000";//��й�ȣ, MySQL ���� ��й�ȣ �Է�
	char *database = "pc_jjim";//DB�� pc_jjim���� ������
	int port = 3306;//DB�� ��Ʈ, 3306�� �⺻�̴�

public:
	Link_DB()//���۸� �ؿ� ������
	{
		conn = mysql_init(NULL);	//Visual studio�� Workbench�� ����

		if (!mysql_real_connect(conn, server, user, password, database, port, NULL, 0))
		{
			fprintf(stderr, "%s ", mysql_error(conn));			//����ó���� (������ ���� �ʾ������ �����޼��� ��� �� ����)
			exit(1);
		}
	}
	void disconnDB()//���۸� �ؿ� ���� ����
	{
		mysql_close(conn);	//VIsual studio�� Workbench ���� ����
	}

	string* sendQuery(char* query)//�̺κ��� ���۸��ؿ����� ���� �ٲ��
	{
		mysql_query(conn, query);	//Workbench�� query�� ����
		res = NULL;
		res = mysql_store_result(conn);//���۸� �ؿ� �ҽ�
		if(res!=NULL)
		{
			int length = (res)->row_count;
			string* temp = new string[length+1];
			
			int num_fields = mysql_num_fields(res);//���۸� �ؿ� �ҽ�
			int cnt = 0;
			while ((row = mysql_fetch_row(res)))//���۸� �ؿ� �ҽ�
			{
				string rows = "";
				for (int i = 0; i < num_fields; i++)
				{
					if (row[i] != NULL)
					{
						rows = rows + row[i] + "^";
					}
					else 
						rows += "NULL^";
				}
				temp[cnt] = rows;
				cnt++;
				
			}
			temp[cnt] = "\0";
			return temp;
		}
		else
		{
			string *temp = new string[1];
			temp[0] = "\0";
			return temp;
		}
	}



};
