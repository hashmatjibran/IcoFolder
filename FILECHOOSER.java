
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.filechooser.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import net.sf.image4j.codec.ico.ICOEncoder;
import javax.imageio.ImageIO;


public class FILECHOOSER implements ActionListener{
 String selectedFolders[] =new String[30];
 String outImgPath[] =new String[30];
 String cmdCommand="";
 String Imagepath="";
 String q1="//";
 String q2="desktop";
 String q3=".ini";
 File outImgFile[]=new File[30];
 BufferedImage iconImages[];
 //BufferedImage iconImages[] =new BufferedImage[25];
//String line;
JLabel outputcmd = new JLabel();
JFrame frame=new JFrame("ICO Folder");

//fileChooser Constructor which will be rendering our frame and displaying GUI for user

FILECHOOSER()
{

frame.setSize(800,800);
FlowLayout layout =new FlowLayout();
frame.setLayout(layout);

frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
JLabel l1= new JLabel("Select Image :");
l1.setBounds(200,100,300,200);
JButton b1 =new JButton("image");
b1.setBounds(500,100,200,200);
JLabel l2= new JLabel("Select Folders :");
l2.setBounds(200,350,300,200);
JButton b2= new JButton ("open");
b2.setBounds(500,350,200,200);
JButton b3= new JButton ("Apply");
b3.setBounds(400,500,200,200);


outputcmd.setBounds(100,450,600,100);
 frame.add(outputcmd);

frame.add(l1);
frame.add(b1);
frame.add(l2);
frame.add(b2);
frame.add(b3);
frame.setVisible(true);


b1.addActionListener(this);
b2.addActionListener(this);
b3.addActionListener(this);
//outputcmd.addActionListener(this);
}

//Constructor ends here


//Check for user Clicks
public void actionPerformed(ActionEvent e){
  String command = e.getActionCommand();
  if (command.equals("image")) {

//creating JFileChooser object to get the selected file information

JFileChooser chooseFile= new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
chooseFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
chooseFile.showOpenDialog(null);
 Imagepath = chooseFile.getSelectedFile().getPath();
 
 String ImageName = chooseFile.getSelectedFile().getName();
 ImageName = ImageName.substring(0,ImageName.length()-4);
 System.out.println(ImageName);
 



File ImageFile =new File(Imagepath);
//creating an input stream to read the user Selected image
  try{
  FileInputStream inputStream = new FileInputStream(ImageFile);
   BufferedImage inputImage = ImageIO.read(inputStream);

   int imageWidth = inputImage.getWidth();
   int imageHeight =inputImage.getHeight();
   
//   it is being hard coded change it to soft code
   int cols = 5;
   int rows= 5;
   int chunks= cols*rows;
   int chunkWidth =(int)imageWidth/5;
   int chunkHeight= (int)imageHeight/5;
   int count=0;
   
 BufferedImage iconImages[] =new BufferedImage[chunks];

for (int x = 0; x < rows; x++) {
for (int y = 0; y < cols; y++) {
//Initialize the image array with image chunks
iconImages[count] = new BufferedImage(chunkWidth, chunkHeight, inputImage.getType());

// draws the image chunk
Graphics2D gr = iconImages[count++].createGraphics();
gr.drawImage(inputImage, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight*x, chunkWidth*y+chunkWidth, chunkHeight*x+chunkHeight, null);
gr.dispose();
}
}
outputcmd.setText("Splitting done");

//creating ICO files for setting to the folders.

for (int i = 0; i < iconImages.length; i++) {

//creating new icon files 
File file = 
	outImgFile[i] =new File("C:\\Users\\hshmt\\Desktop\\"+ImageName + i + ".ico");

outImgPath[i]=outImgFile[i].getAbsolutePath();

System.out.println(outImgFile[i]);

 ICOEncoder.write(iconImages[i], outImgFile[i]);
}

outputcmd.setText("Image converted successfully");

}catch(IOException exp){outputcmd.setText("error processing image");}
catch(Exception abc){outputcmd.setText("This Image is not good enough to Load! please try with new Image .");}
  }

  else if(command.equals("open")){

    JFileChooser chooseFolder= new JFileChooser();
    chooseFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    chooseFolder.setMultiSelectionEnabled(true);
        chooseFolder.showOpenDialog(null);
File folders[] = chooseFolder.getSelectedFiles();
//int Tfolders = folders.length;
System.out.println("total folders selected :- "+ folders.length);

int t = 0;

// set the label to the path of the selected files
while (t < folders.length){

selectedFolders[t]=folders[t].getPath();



System.out.println(selectedFolders[t]);


cmdCommand="attrib -h -r "+selectedFolders[t]+q1+q2+q3;



try{

      Process processalpha = Runtime.getRuntime().exec(cmdCommand);

// wait to process the file ;


File presentFile =new File(selectedFolders[t]+q1+q2+q3);

if (presentFile.exists() && !presentFile.canWrite() )
{

cmdCommand="attrib -h -R "+selectedFolders[t]+q1+q2+q3 ;

    Process process = Runtime.getRuntime().exec(cmdCommand);
    presentFile.setWritable(true);
  //presentFile1.createNewFile();

// wait

  FileWriter myWriter1 = new FileWriter(presentFile,false);

       myWriter1.write("[.ShellClassInfo] \n IconResource="+outImgPath[t]+",0 \n [ViewState] \n Mode= \n Vid= ");
       
       myWriter1.close();
       presentFile.setWritable(false);
       cmdCommand="attrib +h +s +r "+selectedFolders[t]+q1+q2+q3;

       process = Runtime.getRuntime().exec(cmdCommand);
       System.out.println("written to file success.. if .");
     }


else if(presentFile.exists() && presentFile.canWrite())

{

cmdCommand="attrib -h -r "+selectedFolders[t]+q1+q2+q3 ;
  Process process = Runtime.getRuntime().exec(cmdCommand);

// Wait 

  FileWriter myWriter2 = new FileWriter(presentFile,false);

       myWriter2.write("[.ShellClassInfo] \n IconResource="+outImgPath[t]+",0 \n [ViewState] \n Mode= \n Vid= ");
 myWriter2.close();
 cmdCommand="attrib +h +s +r "+selectedFolders[t]+q1+q2+q3;
       process = Runtime.getRuntime().exec(cmdCommand);
       System.out.println("written to file success else if ...");

}

   else {
       presentFile.createNewFile();
     presentFile.setWritable(true);
//     cmdCommand="attrib -H -S -R "+selectedFolders[t]+q1+q2+q3 ;
//         Process process = Runtime.getRuntime().exec(cmdCommand);

       FileWriter myWriter = new FileWriter(presentFile,false);

            myWriter.write("[.ShellClassInfo] \n IconResource="+outImgPath[t]+",0 \n [ViewState] \n Mode= \n Vid= ");
            myWriter.close();

            cmdCommand="attrib +H +S +R "+selectedFolders[t]+q1+q2+q3;

            Process process = Runtime.getRuntime().exec(cmdCommand);

            System.out.println("file Created and written.");
     }
   }catch(IOException e5){System.out.println("IOException found");}
   //catch(FileNotFoundException e6){System.out.println("File not found exception");}
     //catch(FileNotFoundException fnfe){fnfe.printStackTrace();}

//       cmdCommand="attrib +H +S +r "+outImgPath[t]+q1+q2+q3;

       t++;
   }
//catch(FileNotFoundException fnf){System.out.println("oops  something went Wrong");}
//  Process process = Runtime.getRuntime().exec(cmdCommand);
//}catch (IOException e4) {
//         e4.printStackTrace();}
//System.out.println(selectedFolders[t]);

}

}

//else if(command.equals("Apply")){

//   cmdCommand="ping www.codejava.net";
//try{
//  BufferedReader reader = new BufferedReader(
//        new InputStreamReader(process.getInputStream()));
//  String line;
//  while ((line = reader.readLine()) != null) {

//outputcmd.setText(line);
  //  outputcmd.setText=line;

//}
//}catch (IOException e3) {
  //       e3.printStackTrace();}

  //}

 public static void main(String[] args)
 {
FILECHOOSER f= new FILECHOOSER();

 }

  }
