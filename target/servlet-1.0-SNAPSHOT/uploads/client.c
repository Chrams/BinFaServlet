#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <arpa/inet.h>

char user_name[100];

int valaszolhat(int serverFD) {
  char str[100];
  memset(str,0,100);

  printf("%s: ",user_name);
  gets(str);
  send(serverFD,str,strlen(str),0);
  if(strcmp(str,"kilépek")==0 || strcmp(str,"vége")==0)
  {
    return -1;
  }
  return 0;
}

int startup(int serverFD)
{
  char str[100];
  memset(str,0,100);

  puts("\nFelhasználó név:");
  gets(user_name);
  send(serverFD,user_name,strlen(user_name)>255?254:strlen(user_name),0);
  if(strcmp(user_name,"kilépek")==0 || strcmp(user_name,"vége")==0)
  {
    return -1;
  }
  //puts("Elküldtük a nevét\n");
  puts("Válassz egy szobát! (szoba:szobanév)");

  gets(str);
  // nem lehet tetszőleges hosszúságú az üzenet
  send(serverFD,str,strlen(str)>255?254:strlen(str),0);

  if(strcmp(str,"kilépek")==0 || strcmp(str,"vége")==0)
  {
    return -1;
  }
  return 0;
}

void client(int serverFD)
{
  int n = 0;
  char recvBuff[1024];
  memset(recvBuff, 0,1024);
  int size_of_msg;
  //csak a tényleges üzenet méretének megfelelő karaktert olvasunk ki
  //a feltorlódást elkerülendő
  //printf("Sikerült csatlakozni a szerverhez.%d\n",serverFD);
  recv(serverFD,recvBuff,1,0);
  size_of_msg = recvBuff[0];
  memset(recvBuff, 0,1024);
  // a bejelentkezés előtt a szobák listáját megkapja
  recv(serverFD, recvBuff, size_of_msg, 0);
  fputs(recvBuff, stdout);
  //a felhasználónév megadása és szobaválasztás, esetleg kilépés
  if(startup(serverFD)==-1)
  {
    return;
  }

  while(1)
  {
    memset(recvBuff, 0,1024);
    recv(serverFD,recvBuff,1,0);
    size_of_msg = recvBuff[0];
    memset(recvBuff, 0,1024);
    if ( (n = recv(serverFD, recvBuff, size_of_msg, 0)) > 0)
    {
      recvBuff[n] = 0;
      //printf("\n-----------%s\n",recvBuff);
        if(strcmp(recvBuff,"invalidroom")==0)
        {
          puts("Nem jó a szoba kiválasztása!\n");
          return;
        }
        else if(strcmp(recvBuff,"rest")==0)
        {
          puts("...");
          continue;
        }
        else if(strcmp(recvBuff,"firstwait")==0)
        {
          puts("Üres a szoba, még nem üzenhet.");
          continue;
        }
        else if(strcmp(recvBuff,"firststart")==0)
        {
          puts("Küldjön a többieknek üzenetet!");
          if(valaszolhat(serverFD) == -1)
          {//kilépett vagy végét jelzett
            return;
          }
        }
        else if(strcmp(recvBuff,"vége")==0)
        {
          return;
        }else if(fputs(recvBuff, stdout) == EOF)
        {
            printf("\n Error : Fputs error\n");
        }
    }
    if(n < 0)
    {
        printf("\n Read error \n");
    }
  }//while end
}

int main(int argc, char *argv[])
{
    int serverFD = 0;
    struct sockaddr_in serv_addr;

    if(argc != 3)
    {
        printf("\n Usage: %s <ip of server> <port> \n",argv[0]);
        return 1;
    }


    if((serverFD = socket(AF_INET, SOCK_STREAM, 0)) < 0)
    {
        printf("\n Error : Could not create socket \n");
        return 1;
    }

    memset(&serv_addr, '0', sizeof(serv_addr));

    serv_addr.sin_family = AF_INET;
    //htons gazdagép -> hálózati 
    serv_addr.sin_port = htons(atoi(argv[2]));

    if(inet_pton(AF_INET, argv[1], &serv_addr.sin_addr)<=0)
    {
        printf("\n Error:  inet_pton error occured\n");
        return 1;
    }

    if( connect(serverFD, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0)
    {
       printf("\n Error : Connect Failed \n");
       return 1;
    }

    client(serverFD);
    close(serverFD);
    return 0;
}
