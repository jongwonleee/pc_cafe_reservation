using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace PCProgram
{
    public partial class JoinForm : Form
    {
        SocketLink socketLink;
        public JoinForm(SocketLink socket)
        {
            InitializeComponent();
            socketLink = socket;
        }

        private void button1_Click(object sender, EventArgs e)
        {//회원가입 버튼 클릭시
            string id = text_id.Text;
            string pw = text_pw.Text;
            string name = text_name.Text;
            string x = text_x.Text;
            string y = text_y.Text;
            string sit = "00000000000000000000000000000000000000000000000000";
            string query = "Insert into owner (ID,PW,name_room,seatnum,loc_x,loc_y,socket_num) values ('" + id + "','" + pw + "','" + name + "','" + sit + "'," + x + "," + y + "," + 0 + ")";
            text_id.Text = query;
            socketLink.changeFrmNum(2);
            socketLink.send(query);
            this.Close();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            this.Close();
        }
    }
}
