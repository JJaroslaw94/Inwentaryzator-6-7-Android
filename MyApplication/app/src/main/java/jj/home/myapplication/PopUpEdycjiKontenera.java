package jj.home.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class PopUpEdycjiKontenera extends AppCompatActivity {

    String nazwaAktualnegoInv, nazwaAktualnegoPliku, idKontenera;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_pop_up_edycji_kontenera);

        DisplayMetrics rozmiarWyswietlacza = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(rozmiarWyswietlacza);

        int szerokosc = rozmiarWyswietlacza.widthPixels;
        int wysokosc = rozmiarWyswietlacza.heightPixels;

        getWindow().setLayout((int)(szerokosc*0.7),(int) (wysokosc*0.5));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nazwaAktualnegoInv = extras.getString("nazwaAktualnegoInv");
            nazwaAktualnegoPliku = extras.getString("nazwaAktualnegoPliku");
            idKontenera = extras.getString("idKontenera");
        }
        TextView Text = findViewById(R.id.textView6);
        Text.setText(nazwaAktualnegoInv);



        Button BtUsun = findViewById(R.id.button5);
        BtUsun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CzyIstniejePlik(nazwaAktualnegoPliku))
                {
                    File file = getBaseContext().getFileStreamPath(nazwaAktualnegoPliku);
                    file.delete();
                }

                XmlSerializer serializer = Xml.newSerializer();
                StringWriter writer = new StringWriter();

                Document doc = null;
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db;

                if (CzyIstniejePlik("zasobniki.xml"))
                {
                    try {
                        serializer.setOutput(writer);
                        serializer.startDocument("UTF-8", true);
                        serializer.startTag("", "zasobniki");

                        // Fragment kodu wczytujacego plik xml przed dodaniem nowych elementow


                        db = dbf.newDocumentBuilder();
                        FileInputStream fis = openFileInput("zasobniki.xml");
                        doc = db.parse(fis);

                        NodeList zasobniki = doc.getElementsByTagName("zasobnik");

                        for (int i = 0; i < zasobniki.getLength(); i++) {

                            Node city = zasobniki.item(i);

                            if (city.getNodeType() == Element.ELEMENT_NODE) {
                                Element eElement = (Element) city;
                                System.out.println(eElement.getAttribute("id"));
                                if (eElement.getAttribute("id").equals(idKontenera)) {
                                    System.err.println("sdsd");
                                    city.getParentNode().removeChild(city);
                                }else {
                                    serializer.startTag("", city.getNodeName());
                                    serializer.attribute("", "id", Integer.toString(i));

                                    NodeList cityInfo = city.getChildNodes();
                                    for (int j = 0; j < cityInfo.getLength(); j++) {
                                        Node info = cityInfo.item(j);
                                        serializer.startTag("", info.getNodeName());
                                        serializer.text(info.getTextContent());
                                        serializer.endTag("", info.getNodeName());
                                    }
                                    serializer.endTag("", city.getNodeName());

                                }
                            }


                        }
                        fis.close();

                        serializer.endDocument();
                        String result = writer.toString();
                        ZapsywanieWczytywanie.ZapisDoPliku(context, "zasobniki.xml", result);

                        finish();
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
            }
        });

        Button btnEdytuj = findViewById(R.id.button4);
        btnEdytuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPopUp = new Intent(".EdytowanieKonteneraEdycja");

                intentPopUp.putExtra( "nazwaAktualnegoPliku",  nazwaAktualnegoPliku);
                intentPopUp.putExtra( "nazwaAktualnegoInv",  nazwaAktualnegoInv);
                intentPopUp.putExtra( "idKontenera",  idKontenera);

                startActivity(intentPopUp);
            }
        });
    }

    public boolean CzyIstniejePlik(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }
}
