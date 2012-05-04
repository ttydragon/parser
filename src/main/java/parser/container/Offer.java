package parser.container;

/**
 * User: Andrey
 * Date: 04.05.12
 * Time: 13:48
 */
public class Offer {
    public String propertiId="";
    public String arrivalDate="";
    public String departureDate="";
    public String nights="";
    public String baserate="";
    public String totalbaserate="";
    public String offerbaserate="";
    public String offertotalrate="";
    public String currensy="";
    public OfferLinks links;

    public Offer() {
        this.links = new OfferLinks();
    }

    
    public String toFlatString(){
        String s =
        (((propertiId!=null)?propertiId:"NULL") + " | ")+
        (((arrivalDate!=null)?arrivalDate:"NULL") + " | ")+
        (((departureDate!=null)?departureDate:"NULL") + " | ")+
        (((nights!=null)?nights:"NULL") + " | ")+
        (((baserate!=null)?baserate:"NULL") + " | ")+
        (((totalbaserate!=null)?totalbaserate:"NULL") + " | ")+
        (((offerbaserate!=null)?offerbaserate:"NULL") + " | ")+
        (((offertotalrate!=null)?offertotalrate:"NULL") + " | ")+
        (((currensy!=null)?currensy:"NULL") + " | ")+
        (((links.book_now!=null)?links.book_now:"NULL") + " | ")+
        (((links.send_equiry!=null)?links.send_equiry:"NULL") + " | ")+
        (((links.listing!=null)?links.listing:"NULL") + " ");

        return s.trim().replace('\n',' ');

    }
}
