using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Threading;

namespace PCProgram
{
    public partial class StartForm : Form
    {
        public string id;
        public string pw;
        public bool rtn = false;
        public SocketLink socket;
        StartForm ffrm = null;
        Thread reciever;
        public string recv;

        public StartForm()
        {
            InitializeComponent();
            socket = new SocketLink(this);
            socket.Connect("192.168.1.11", 3500);//이 부분에 서버의 아이피를 입력해준다
            ffrm = this;
        }

        private void button1_Click(object sender, EventArgs e)//로그인 버튼 클릭
        {
            reciever = new Thread(new ThreadStart(returned));
            reciever.Start();
            string _id = text_id.Text;
            string _pw = text_pw.Text;
            string query = "select name_room,seatnum from owner where ID = '" + _id + "' and PW = '" + _pw + "'";
            socket.changeFrmNum(0);
            socket.send(query);
            


        }

        delegate void SetCallback();//델리게이트를 이용해 쓰레드 에러 제거는 구글링을 해왔다

        private void returned()
        {
            if (rtn)
            {

                if (this.InvokeRequired)//이부분은 구글링이다
                {
                    SetCallback d = new SetCallback(returned);
                    this.Invoke(d, new object[] { });
                }
                else
                {
                    if (recv.Contains("null"))
                    {//로그인 실패시
                        MessageBox.Show("로그인 실패\n다시 입력해주세요", "로그인 에러");
                        text_id.Text = "";
                        text_pw.Text = "";
                        reciever.Abort();
                    }
                    else
                    {//성공시
                        PCRoom frm = new PCRoom(this);
                        id = text_id.Text;
                        pw = text_pw.Text;
                        frm.Show();
                        this.Hide();
                        reciever.Abort();
                    }
                }

            }

        }

        private void button2_Click(object sender, EventArgs e)
        {
            JoinForm frm = new JoinForm(socket);
            frm.Show();
        }

        private void StartForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            socket.disconnect();
        }

        private void StartForm_Load(object sender, EventArgs e)
        {

        }
    }
}
