#pragma comment (lib, "libmySQL.lib")
#pragma comment (lib, "ws2_32.lib")

#include <iostream>
#include <string>
#include <string.h>
#include <my_global.h>
#include <mysql.h>

using namespace std;
//전체적인 틀은 구글링해서 복사해옴
class Link_DB
{
private:
	//변수명이 구글링과 같을 수 있음
	MYSQL *conn;
	MYSQL_RES *res = NULL;
	MYSQL_ROW row;
	MYSQL_FIELD *fields;

	int len;
	//이 부분이 DB와 연결해주는 부분임
	char *server = "localhost";//서버는 로컬호스트를씀
	char *user = "root";//유저이름임, MySQL 유저 이름 입력
	char *password = "0000";//비밀번호, MySQL 접속 비밀번호 입력
	char *database = "pc_jjim";//DB명 pc_jjim으로 만들음
	int port = 3306;//DB의 포트, 3306이 기본이다

public:
	Link_DB()//구글링 해온 생성자
	{
		conn = mysql_init(NULL);	//Visual studio와 Workbench를 연결

		if (!mysql_real_connect(conn, server, user, password, database, port, NULL, 0))
		{
			fprintf(stderr, "%s ", mysql_error(conn));			//예외처리문 (연결이 되지 않았을경우 에러메세지 출력 및 종료)
			exit(1);
		}
	}
	void disconnDB()//구글링 해온 접속 종료
	{
		mysql_close(conn);	//VIsual studio와 Workbench 연결 해지
	}

	string* sendQuery(char* query)//이부분은 구글링해왔지만 많이 바꿨다
	{
		mysql_query(conn, query);	//Workbench에 query문 전송
		res = NULL;
		res = mysql_store_result(conn);//구글링 해온 소스
		if(res!=NULL)
		{
			int length = (res)->row_count;
			string* temp = new string[length+1];
			
			int num_fields = mysql_num_fields(res);//구글링 해온 소스
			int cnt = 0;
			while ((row = mysql_fetch_row(res)))//구글링 해온 소스
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
