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
    public partial class PCRoom : Form
    {
        public int[] sit = new int[50];
        StartForm frm;
        SocketLink socketLink;
        Thread update;
        public Label[] sits = new Label[50];
        public PCRoom(StartForm _frm)
        {
            InitializeComponent();
            frm = _frm;
            sits[0] = sit1;
            sits[1] = sit2;
            sits[2] = sit3;
            sits[3] = sit4;
            sits[4] = sit5;
            sits[5] = sit6;
            sits[6] = sit7;
            sits[7] = sit8;
            sits[8] = sit9;
            sits[9] = sit10;
            sits[10] = sit11;
            sits[11] = sit12;
            sits[12] = sit13;
            sits[13] = sit14;
            sits[14] = sit15;
            sits[15] = sit16;
            sits[16] = sit17;
            sits[17] = sit18;
            sits[18] = sit19;
            sits[19] = sit20;
            sits[20] = sit21;
            sits[21] = sit22;
            sits[22] = sit23;
            sits[23] = sit24;
            sits[24] = sit25;
            sits[25] = sit26;
            sits[26] = sit27;
            sits[27] = sit28;
            sits[28] = sit29;
            sits[29] = sit30;
            sits[30] = sit31;
            sits[31] = sit32;
            sits[32] = sit33;
            sits[33] = sit34;
            sits[34] = sit35;
            sits[35] = sit36;
            sits[36] = sit37;
            sits[37] = sit38;
            sits[38] = sit39;
            sits[39] = sit40;
            sits[40] = sit41;
            sits[41] = sit42;
            sits[42] = sit43;
            sits[43] = sit44;
            sits[44] = sit45;
            sits[45] = sit46;
            sits[46] = sit47;
            sits[47] = sit48;
            sits[48] = sit49;
            sits[49] = sit50;

            socketLink = frm.socket;
            socketLink.setMainFrm(this);

            //로그인을 하면서 받아온 정보들로 기본 정보를 프로그램에 뿌림
            string temp = frm.recv;
            string name = temp.Substring(0, temp.IndexOf('^'));
            lb_info.Text = name + " 입니다! 환영합니다.";
            temp = temp.Substring(temp.IndexOf('^')+1);
            
            string sitstring = temp.Substring(0, temp.IndexOf('^'));
            returned(sitstring);

            
        }

        
        private void button1_Click(object sender, EventArgs e)
        {
            frm.Close();
            this.Close();
            
        }



        private void sit_Click(object sender, EventArgs e)
        {//좌석 버튼을 눌렀을 때 정보가 변하고 서버로 변한 정보를 업데이트 함
            Label clicked = (Label)sender;

            if(sit[clicked.TabIndex-1]==0)
            {
                sit[clicked.TabIndex - 1] = 1;
                clicked.BackColor = Color.DimGray;
            }
            else
            {
                sit[clicked.TabIndex - 1] = 0;
                clicked.BackColor = Color.Silver;
            }
            string sitlist="";
            for (int i = 0; i < 50;i++ )
            {
                sitlist = sitlist + sit[i].ToString();
            }
            string query = "update owner set seatnum = '" + sitlist + "' where id = '" + frm.id + "'";
                socketLink.send(query);
        }

        private void PCRoom_Load(object sender, EventArgs e)
        {
            
        }

        public void returned(string recv)
        {//서버로부터 좌석 정보를 받아왔을 때 프로그램을 업데이트시키는 쓰레드
            char[] sitlist = recv.ToCharArray();
            for (int i = 0; i < 50; i++)
            {
                sit[i]  = sitlist[i] - '0';
 
                if (sit[i]==0)
                    sits[i].BackColor = Color.Silver;
                else if(sit[i]==1)
                        sits[i].BackColor = Color.DimGray;
                else
                    sits[i].BackColor = Color.HotPink;  
            }
        }

    }
}
