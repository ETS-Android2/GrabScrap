package com.example.crud;

public class MyDataModel {

    private String name;
    private String pname, address, price, cinfo, desc, login_id, uid, img;





    public void setName(String name) {
        this.name = name;
    }

    public String getName()
    {return pname;}

    public String getAddress()
    {return this.address;}

    public String getPrice()
    {return price;}

    public String getCinfo()
    {return cinfo;}

    public String getDesc()
    {return desc;}

    public String getLogin_id()
    {return login_id;}

    public String getuid()
    {return uid;}

    public String getimg()
    {return img;}
//    public String getId() {
//        return id;
//    }

    public void setpname(String fpname)
    {this.pname = fpname;}

    public void address(String faddress)
    {this.address = faddress;}

    public void price(String fprice)
    {this.price = fprice;}

    public void cinfo(String fcinfo)
    {this.cinfo = fcinfo;}

    public void desc(String fdesc)
    {this.desc = fdesc;}

    public void loginid(String id)
    {this.login_id = id;}

    public void setuid(String fuid)
    {this.uid = fuid;}

    public void setimg(String fimg)
    {this.img = fimg;
    System.out.println("imgmgmgmg " + this.img);}
//    public void setCountry(String id) {
//        this.id=id;
//    }



}
