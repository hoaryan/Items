class BackElement {

    public static int HOME_VIEW = 0
    public static int STD_VIEW = 1
    public static int SIMPLE_SCHEDULE_VIEW = 2

    public int view
    public int id


    public boolean equals( Object obj )
    {
      if ( obj instanceof BackElement ) {
        BackElement bkel = (BackElement)obj;
        return (view == bkel.view) && (id == bkel.id);   
      }
      return super.equals(obj);
    }


    public String toString() {
        return "[VIEW: " + this.view + ", ID: " + this.id + "]"
    }

}

