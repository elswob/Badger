<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name='layout' content='main'/>
  <title>${grailsApplication.config.projectID} consortium</title>
  <parameter name="consortium" value="selected"></parameter>
  </head>

  <body>
    <h1>Consortium information:</h1>
    <table class="table_basic">
    
    <tr>
    <td> 
    <a href = "http://genepool.bio.ed.ac.uk/" target='_blank'><img src="${resource(dir: 'images', file: 'gp.jpg')}" height="70" width="145"/></a>
    <a href = "http://www.ed.ac.uk" target='_blank'><img src="${resource(dir: 'images', file: 'homecrest.gif')}" height="70" width="70"/></a></td>
    <td><a href="http://www.nematodes.org/" target='_blank'>Mark Blaxter</a><br>Ben Elsworth</td>
    <td><a href="mailto:ben.elsworth@ed.ac.uk">mark.blaxter@ed.ac.uk</a><br><a href="mailto:ben.elsworth@ed.ac.uk">ben.elsworth@ed.ac.uk</a></td>
    </tr>
    
    <tr>
    <td><a href = "http://www.cam.ac.uk/" target='_blank'><img src="${resource(dir: 'images', file: 'University-of-Cambridg.png')}" height="70" width="250"/></a></td>
    <td><a href = "http://www.zoo.cam.ac.uk/zoostaff/brakefield.html" target='_blank'>Paul Brakefield</a><br>Ullasa Kodandaramaiah</td>
    <td><a href="mailto:pb499@cam.ac.uk">pb499@cam.ac.uk<br><a href="mailto:uk214@cam.ac.uk">uk214@cam.ac.uk</a></td>
    </tr>
    
    <tr>
    <td><a href = "http://www.yale.edu/" target='_blank'><img src="${resource(dir: 'images', file: 'yale.jpg')}" height="70" width="250"/></a></td>
    <td><a href="http://lepdata.org/monteiro/" target='_blank'>Antonia Monteiro</a><br>Bethany Wasik<br><a href="http://bbs.yale.edu/people/michael_nitabach-1.profile" target='_blank'>Michael N. Nitabach</a></td>
    <td><a href="mailto:antonia.monteiro@yale.edu">antonia.monteiro@yale.edu</a><br><a href="mailto:bethany.wasik@gmail.com">bethany.wasik@gmail.com</a><br><a href="mailto:michael.nitabach@yale.edu">michael.nitabach@yale.edu</td>
    </tr>
    
    <tr>
    <td><a href = "http://www.igc.gulbenkian.pt/" target='_blank'><img src="${resource(dir: 'images', file: 'logoigc50.jpg')}" height="70" width="350"/></a></td> 
    <td>Patricia Beldade</td>
    <td><a href="mailto:pbeldade@igc.gulbenkian.pt">pbeldade@igc.gulbenkian.pt</a></td>
    </tr>
    
    <tr>
    <td><a href="http://www.wur.nl/UK/" target='_blank'><img src="${resource(dir: 'images', file: 'UNIVERSITY_web-rgb.gif')}" height="70" width="350"/></a></td>
    <td><a href="http://www.vcard.wur.nl/Views/Profile/View.aspx?id=35477&ln=eng" target='_blank'>Bas Zwaan</a></td>
    <td><a href="mailto:bas.zwaan@wur.nl">bas.zwaan@wur.nl</a></td>
    </tr>
    
    <tr>
    <td><a href="http://www.uni-greifswald.de/?L=1" target='_blank'><img src="${resource(dir: 'images', file: 'siegel_testheader.gif')}" height="70" width="350"/></a></td>
    <td><a href="http://www.mnf.uni-greifswald.de/index.php?id=1772" target='_blank'>Klaus Fischer</a></td>
    <td><a href="mailto:klaus.fischer@uni-greifswald.de">klaus.fischer@uni-greifswald.de</a></td>
    </tr>
    
    <tr>
    <td><a href="http://www.liv.ac.uk/" target='_blank'><img src="${resource(dir: 'images', file: 'UniLiv_logo.jpg')}" height="70" width="350"/></a></td>
    <td><a href="http://www.liv.ac.uk/integrative-biology/staff/arjen-van-t-hof/" target='_blank'>Arjen Van 't Hof</a></td>
    <td><a href="mailto:A.E.Van-T-Hof@liverpool.ac.uk">A.E.Van-T-Hof@liverpool.ac.uk</a></td>
    </tr>
    
    <tr>
    <td><a href="http://www.uci.edu/" target='_blank'><img src="${resource(dir: 'images', file: 'uci_textbanner.png')}" height="70" width="350"/></a></td>
    <td><a href="http://wfitch.bio.uci.edu/~tdlong/" target='_blank'>Anthony Long</a></td>
    <td><a href="mailto:tdlong@uci.edu">tdlong@uci.edu</a></td>
    </tr>
    </table>
  </body>
</html>