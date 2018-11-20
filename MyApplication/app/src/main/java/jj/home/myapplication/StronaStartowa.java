package jj.home.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class StronaStartowa extends AppCompatActivity {

    public String nazwaAktualnegoInv;
    public String nazwaAktualnegoPliku;
    public String nazwaAktualnegoTagu;
    CustomGridViewActivity adapterViewAndroid;
    GridView androidGridView;

    int[] NazwyObrazkowGrid;
    String[]  NazwyKontenerowGrid;
    String[]  NazwyPlikowGrid;
    String[]  NazwyTagowGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strona_startowa);


        UzupelnienieGridView();
        WypelnijGrid();

        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                Toast.makeText(StronaStartowa.this, "GridView Item: " + NazwyKontenerowGrid[i], Toast.LENGTH_SHORT).show();
                if (NazwyKontenerowGrid[i].equals("Dodaj"))
                {
                    Intent intentDodajacyKontener = new Intent(".StronaDodawaniaKontenerow");
                    startActivity(intentDodajacyKontener);
                }else
                {
                    //TODO

                    nazwaAktualnegoInv = NazwyKontenerowGrid[i];
                    nazwaAktualnegoPliku = NazwyPlikowGrid[i];
                    nazwaAktualnegoTagu = NazwyTagowGrid[i];

                    Intent intentPrzegladajacyPrzedmioty = new Intent(".StronaPrzedmiotowKontenera");
                    intentPrzegladajacyPrzedmioty.putExtra( "nazwaAktualnegoInv",  nazwaAktualnegoInv);
                    intentPrzegladajacyPrzedmioty.putExtra( "nazwaAktualnegoPliku",  nazwaAktualnegoPliku);
                    intentPrzegladajacyPrzedmioty.putExtra( "nazwaAktualnegoTagu",  nazwaAktualnegoTagu);
                    startActivity(intentPrzegladajacyPrzedmioty);
                }
            }
        });

    }

    public void onResume(){
        super.onResume();
        UzupelnienieGridView();
        WypelnijGrid();
    }
    public void onBackPressed()
    {
       System.exit(0);
    }

    public void UzupelnienieGridView() {

        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;

        if (CzyIstniejePlik("zasobniki.xml"))
        {
            try {
                db = dbf.newDocumentBuilder();
                FileInputStream fis = openFileInput("zasobniki.xml");
                doc = db.parse(fis);

                NodeList zasobniki = doc.getElementsByTagName("zasobnik");
                String[] NazwyKontenerowGridtemp = new String[zasobniki.getLength()+1];
                String[] NazwyPlikowGridtemp = new String[zasobniki.getLength()+1];
                String[] NazwyTagowGridtemp = new String[zasobniki.getLength()+1];
                int[] NazwyObrazkowGridtemp = new int[zasobniki.getLength()+1];
                for (int i = 0; i < zasobniki.getLength(); i++) {

                    Node city = zasobniki.item(i);
                    NodeList cityInfo = city.getChildNodes();
                    for (int j = 0; j < cityInfo.getLength(); j++) {
                        Node info = cityInfo.item(j);
                        if (info.getNodeName().equals("nazwa")) {
                            NazwyKontenerowGridtemp[i] = info.getTextContent();
                        }else
                        if (info.getNodeName().equals("zdjecie")) {
                            if(info.getTextContent().equals("R.drawable.inv")){
                            NazwyObrazkowGridtemp[i] = R.drawable.inv;}
                        }else
                        if (info.getNodeName().equals("nazwa_pliku")){
                            NazwyPlikowGridtemp[i] = info.getTextContent();
                        }
                        else if (info.getNodeName().equals("wymagane_tagi")){
                            NazwyTagowGridtemp[i] = info.getTextContent();
                        }
                    }
                }
                fis.close();
                NazwyKontenerowGridtemp[zasobniki.getLength()] = "Dodaj";
                NazwyKontenerowGrid = NazwyKontenerowGridtemp;
                NazwyObrazkowGridtemp[zasobniki.getLength()]= R.drawable.dodaj;
                NazwyObrazkowGrid = NazwyObrazkowGridtemp;
                NazwyPlikowGrid = NazwyPlikowGridtemp;
                NazwyTagowGrid = NazwyTagowGridtemp;

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
            NazwyObrazkowGrid = new int[]{R.drawable.dodaj};
            NazwyKontenerowGrid = new String[]{"Dodaj"};
        }
    }

    public void WypelnijGrid(){
        adapterViewAndroid = null;
        adapterViewAndroid = new CustomGridViewActivity(StronaStartowa.this, NazwyKontenerowGrid, NazwyObrazkowGrid);
        androidGridView=(GridView)findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
    }

    public boolean CzyIstniejePlik(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }
}
