using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Windows.Forms;
using System.Drawing;
//이부분은 소켓 클라이언트 부분으로 대부분을 구글링을 해왔다
namespace PCProgram
{
    public class SocketLink
    {
        private Socket socket;
        Thread recieve;
        bool connected = false;
        private StartForm startFrm;
        private PCRoom mainFrm;
        private int frm_num;
        public SocketLink(StartForm _frm)//생성자
        {
            socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);//복사해 온 소스
            startFrm = _frm;
            frm_num = 0;
        }

        public void setMainFrm(PCRoom _frm)//정보를 보낼 폼을 설정
        {
            mainFrm = _frm;
            frm_num = 1;
        }
        public void changeFrmNum(int a)//폼이 바뀌었을때 사용
        {
            frm_num = a;
        }

        public void Connect(string _ip,int port)//서버에 연결, 복사해 온 소스
        {
            IPAddress ip = IPAddress.Parse(_ip);
            IPEndPoint localEndPoint = new IPEndPoint(ip, port);

            try
            {
                socket.Connect(localEndPoint);
                connected = true;
                recieve = new Thread(new ThreadStart(recieving));
                recieve.Start();
            }catch(Exception e)
            {
                MessageBox.Show(e.ToString());
            }
        }

        public void send(string _send)//서버로 보내는 함수
        {
            try
            {
                if (connected)//복사해 온 소스
                {
                    byte[] data = Encoding.UTF8.GetBytes(_send);
                    socket.Send(data);
                }
            }
            catch
            {

            }

        }

        public void disconnect()
        
        
        { 
            {}
        }

        private void recieving()//서버로부터 받아오는 소스, 복사해 온 부분에 많은 것을 더했다
        {
            while(connected)
            {
                byte[] buf = new byte[1024];
                socket.Receive(buf);

                if(buf!=null)
                {
                    string recv = Encoding.Default.GetString(buf);
                    if (frm_num == 0)//열린 폼에 따라 보내주는 정보를 다르게 한다
                    {          
                        startFrm.rtn=true;
                        startFrm.recv = recv;
                    }else if(frm_num ==1)
                    {
                        if(!recv.Contains("null"))
                        {
                            mainFrm.returned(recv);
                        }       
                    }
                    buf = null;
                }
            }
        }

        public bool getConnected()
        {
            return connected;
        }


    }
}
