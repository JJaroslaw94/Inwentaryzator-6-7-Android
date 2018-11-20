package jj.home.myapplication;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class StronaPrzedmiotowKontenera extends AppCompatActivity {

    private ListView list ;
    private ArrayAdapter<String> adapter ;
    private String ListaPrzedmiotow[];
    private String ListaTagow[];
    boolean czySzukam = false;
    String szukana;
   // private String carsSpecs[] = {"1.3","FWD","3 drzwiowy"};
    String nazwaAktualnegoInv;
    String nazwaAktualnegoPliku;
    String nazwaAktualnegoTagu;
    TextView Naglowek;
    TextView editText3s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strona_przedmiotow_kontenera);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nazwaAktualnegoInv = extras.getString("nazwaAktualnegoInv");
            nazwaAktualnegoPliku = extras.getString("nazwaAktualnegoPliku");
            nazwaAktualnegoTagu = extras.getString("nazwaAktualnegoTagu");
        }

        Naglowek = findViewById(R.id.textView2);
        Naglowek.setText("Przedmioty : " + nazwaAktualnegoInv);

        list = findViewById(R.id.listView1);




        UzupelnienieListy();
        WypelnijListe();



        editText3s = findViewById(R.id.editText3);

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                szukana = editText3s.getText().toString();
                if (szukana.equals(""))

                    czySzukam = false;

                else

                    czySzukam = true;

                UzupelnienieListy();
                WypelnijListe();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {

            String start = "start";
                if (ListaPrzedmiotow[i] == "Dodaj")
                {

                    Intent intentDodajacyPrzedmiot = new Intent(".StronaDodawaniaPrzedmiotow");
                    intentDodajacyPrzedmiot.putExtra( "nazwaAktualnegoPliku",  nazwaAktualnegoPliku);
                    startActivity(intentDodajacyPrzedmiot);
                }
                else
                {
                    String[] PodzieloneTagi = nazwaAktualnegoTagu.split(",");
                    int dlugoscTagow = PodzieloneTagi.length;
                    String[] PodzieloneNazwyTagow = ListaTagow[i].split(",");
                    int dlugoscNazw = PodzieloneNazwyTagow.length;

                    int roznicaDlugosci = dlugoscTagow - dlugoscNazw ;
                    String TagiOrazNazwy = "Przedmiot: \n"+"Nazwa: "+ListaPrzedmiotow[i] + "\n";

                    for (int ii = 0;ii < dlugoscTagow ;ii++)
                    {
                        if (roznicaDlugosci <= 0){
                        TagiOrazNazwy = TagiOrazNazwy + PodzieloneTagi[ii] + ": ";
                        TagiOrazNazwy = TagiOrazNazwy + PodzieloneNazwyTagow[ii] +"\n";}
                        else{
                            TagiOrazNazwy = TagiOrazNazwy + PodzieloneTagi[ii] + ": ";
                            if (dlugoscTagow - roznicaDlugosci <= ii){

                            }else
                                TagiOrazNazwy = TagiOrazNazwy + PodzieloneNazwyTagow[ii] +"\n";
                        }
                    }

                    Intent intentPopUp = new Intent(".PopUp");

                    intentPopUp.putExtra( "TagiOrazNazwy",  TagiOrazNazwy);

                    startActivity(intentPopUp);
//TODO
                }
            }
        });
    }

    public void onResume(){
        czySzukam = false;
        super.onResume();
        UzupelnienieListy();
        WypelnijListe();
    }

    public void UzupelnienieListy() {

        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;

        if (CzyIstniejePlik(nazwaAktualnegoPliku))
        {
            try {
                db = dbf.newDocumentBuilder();
                FileInputStream fis = openFileInput(nazwaAktualnegoPliku);
                doc = db.parse(fis);

                NodeList przedmioty = doc.getElementsByTagName("przedmiot");
                String[] NazwyPrzedmiotow = new String[przedmioty.getLength()+1];
                String[] NazwyTagow = new String[przedmioty.getLength()+1];

                int licznik = 0;
                for (int i = 0; i < przedmioty.getLength(); i++) {

                    Node city = przedmioty.item(i);
                    NodeList cityInfo = city.getChildNodes();
                    for (int j = 0; j < cityInfo.getLength(); j++) {
                        Node info = cityInfo.item(j);
                        if (info.getNodeName().equals("nazwa")) {
                            if (czySzukam) {
                                if (info.getTextContent().equals(szukana)) {

                                    NazwyPrzedmiotow[licznik] = info.getTextContent();
                                    licznik++;
                                }
                            } else {
                                NazwyPrzedmiotow[i] = info.getTextContent();
                            }
                        }
                            if (info.getNodeName().equals("tagi")) {
                                if (czySzukam) {
                                    if (info.getTextContent().equals(szukana)) {

                                        NazwyTagow[licznik] = info.getTextContent();
                                        licznik++;
                                    }
                                } else {
                                    NazwyTagow[i] = info.getTextContent();
                                }
                            }

                    }
                }
                fis.close();
                if (czySzukam)
                {
                    ListaPrzedmiotow =  new String[licznik];
                    NazwyPrzedmiotow[licznik] = "Dodaj";
                    for (int o = 0; o < licznik; o++)
                    ListaPrzedmiotow[o] = NazwyPrzedmiotow[o] ;
                }else
                {
                    ListaPrzedmiotow =  new String[przedmioty.getLength()];
                    NazwyPrzedmiotow[przedmioty.getLength()] = "Dodaj";
                    ListaPrzedmiotow = NazwyPrzedmiotow;

                    ListaTagow = new String[przedmioty.getLength()];
                    NazwyTagow[przedmioty.getLength()] = "";
                    ListaTagow = NazwyTagow;
                }


            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            ListaPrzedmiotow = new String[]{"Dodaj"};
        }
    }

    public boolean CzyIstniejePlik(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    public void WypelnijListe(){
        adapter = null;
        ArrayList<String> carL = new ArrayList<String>();
        carL.addAll( Arrays.asList(ListaPrzedmiotow) );
        adapter = new ArrayAdapter<String>(this, R.layout.listview_layout, carL);
        list.setAdapter(adapter);
    }

}
