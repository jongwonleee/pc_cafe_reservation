#define _CRT_SECURE_NO_WARNINGS
//�̺κ��� ���� ���� �κ��ε� ������ �п����� ������ �� ��� �ҽ��̴�
//Ʋ�� �п����� ���� �ҽ� �״���̴�
#include <iostream>
#include <WinSock2.h>
#include <string.h>
#include "Link_DB.h"
#include <string>
#include <sstream>
#pragma comment(lib,"ws2_32.lib")

#define PORT 3500
#define MAX_BUF_SIZE 512

using namespace std;

int len(string*input)
{
	int count = 0;
	while (input[count] != "\0") count++;
	return count;
}

int main()
{
	WSADATA wsdata;

	Link_DB DBconnecter;


	if (WSAStartup(MAKEWORD(2, 2), &wsdata) != 0)
	{
		std::cout << "������ ���� �ʱ�ȭ ����\n\n";
		return 1;
	}

	SOCKET serverSocket;
	serverSocket = socket(AF_INET, SOCK_STREAM, 0);

	if (serverSocket < 0)
	{
		std::cout << "���� ���� ����!" << endl << endl;
		WSACleanup();
		return 1;
	}

	SOCKADDR_IN serverAddress;
	ZeroMemory(&serverAddress, sizeof(serverAddress));

	serverAddress.sin_family = AF_INET;
	serverAddress.sin_port = htons(PORT);
	serverAddress.sin_addr.s_addr = htonl(INADDR_ANY);

	if (bind(serverSocket, (SOCKADDR*)&serverAddress, sizeof(serverAddress)) == SOCKET_ERROR)
	{
		std::cout << "serverSocket�� IP�� PORT�� �ο��ϴµ� ����!" << endl << endl;
		return 1;
	}

	std::cout << "Ŭ���̾�Ʈ�� ������ �����!" << endl << endl;
	if (listen(serverSocket, SOMAXCONN) == SOCKET_ERROR)
	{
		std::cout << "Ŭ���̾�Ʈ�� ���� ��� ����" << endl;
		return 1;
	}
	fd_set read_fds, all_fds;
	FD_ZERO(&read_fds);
	FD_SET(serverSocket, &read_fds);

	int fd_num;
	int read_data, send_data;
	SOCKET client_socket;

	char s_msg_str[MAX_BUF_SIZE + 20];
	char str_client_socket[10];
	char msg_str[MAX_BUF_SIZE];
	bool login_chk = false;

	

	while (1)
	{
		all_fds = read_fds;
		fd_num = select(9, &all_fds, NULL, NULL, NULL);

		if (FD_ISSET(serverSocket, &all_fds))
		{
			if ((client_socket = accept(serverSocket, NULL, NULL)) == INVALID_SOCKET)
			{
				std::cout << "Ŭ���̾�Ʈ�� �����͸� �ְ�޴� ������ ������ �� ����" << endl << endl;
				continue;
			}
			std::cout << client_socket << " Ŭ���̾�Ʈ�� ���Ӽ���" << endl;
			FD_SET(client_socket, &read_fds);
			continue;
		}
		for (int i = 0; i < all_fds.fd_count; i++)
		{

			if (all_fds.fd_array[i] == serverSocket) continue;

			client_socket = all_fds.fd_array[i];

			ZeroMemory(msg_str, MAX_BUF_SIZE);
			read_data = recv(client_socket, msg_str, MAX_BUF_SIZE, 0);

			if (read_data <= 0)
			{
				closesocket(client_socket);
				std::cout << client_socket << " Ŭ���̾�Ʈ�� ������ ����" << endl;
				string query = "update owner set socket_num = 0 where socket_num =" + client_socket;
				DBconnecter.sendQuery((char*)query.c_str());
				FD_CLR(client_socket, &read_fds);
			}
			else
			{
				cout << client_socket << " Ŭ���̾�Ʈ�� ���� �޼���" << msg_str << endl;
				_ultoa(client_socket, str_client_socket, 10);
				//������� ������ �� �ҽ�

				string get_msg = msg_str;

				//���÷κ��� �¼� ������Ʈ �������� ���� �� pc�� ���α׷����� �����ִ� �κ�
				if ((get_msg.find("Update") != string::npos) && (get_msg.find("owner") != string::npos) && (get_msg.find("primenum") != string::npos) && (get_msg.find("set seatnum") != string::npos))
				{
					string query = "select socket_num from owner " + get_msg.substr(get_msg.find("where"));
					string* cls = DBconnecter.sendQuery((char*)query.c_str());
					string sitinfo = get_msg.substr(get_msg.find("'") + 1, 50);
					
					*cls = cls->substr(0, cls->length() - 1);
					int socknum = stoi(*cls);
					SOCKET tmp = socknum;
					cout << "!!" << sitinfo << endl;
					send_data = send(tmp, (char*)sitinfo.c_str(), strlen((char*)sitinfo.c_str()), 0);

				}


				string* recvd = DBconnecter.sendQuery(msg_str);//db�� ������ ���� �� ���� ���� �޴� �κ�

				//���� ���ϰ��� ���� �迭ȭ��Ű�ų� �׳� Ŭ���̾�Ʈ�� �����ִ� �κ�
				if (recvd[0] != "\0")
				{
					
					int ln = len(recvd);
					if (ln == 1)//�ϳ��� ������
					{
						cout << client_socket << " Ŭ���̾�Ʈ�� ���� �޼���" << recvd[0].c_str() << endl;
						recvd[0] = recvd[0] + "\n";
						send_data = send(client_socket, (char*)recvd[0].c_str(), strlen((char*)recvd[0].c_str()), 0);
					}
					else {//�迭�� ������
						string sending = to_string(ln)+"\n";
						send_data = send(client_socket, (char*)sending.c_str(), strlen((char*)sending.c_str()), 0);
						for (int i = 0; i < ln; i++)
						{
							cout << client_socket << " Ŭ���̾�Ʈ�� ���� �޼��� [" <<i+1<<"] : "<< recvd[i].c_str() << endl;
							recvd[i] = recvd[i] + "\n";
							send_data = send(client_socket, (char*)recvd[i].c_str(), strlen((char*)recvd[i].c_str()), 0);
						}
					}

					//pc�� ���α׷��� �������� �� ���� ��ȣ�� db�� �������ִ� ���α׷�
					string get_msg = msg_str;
					if ((get_msg.find("select") >= 0) && (get_msg.find("owner") >= 0) && (get_msg.find("id")))
					{
						string info = recvd[0];
						string name = info.substr(0, info.find('^'));
						string sitinfo = info.substr(info.find('^')+1, info.find_last_of('^')- info.find('^') - 1);
						int a = client_socket;
						cout << sitinfo;
						string query = "update owner set socket_num = " + to_string(a)+ " where name_room = '"+name+"' and seatnum = '" + sitinfo+"'";
						DBconnecter.sendQuery((char*)query.c_str());
					}
				}
				else
				{
					char* temp = "null\n";
					cout << client_socket << " Ŭ���̾�Ʈ�� ���� �޼���" << temp << endl;
					send_data = send(client_socket, temp, strlen(temp), 0);
				}
				ZeroMemory(recvd, sizeof(recvd));
			}
		}
	}


	// �� ���ĺκе� ������ �� �κ�

	closesocket(serverSocket);
	std::cout << "Ŭ���̾�Ʈ���� ������ ����." << endl << endl;
	WSACleanup();
	std::cout << "������ ���� ����" << endl;

	DBconnecter.disconnDB();

	return 0;

}