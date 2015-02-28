package com.ahluwalia.atlas;

import java.util.HashMap;


class flagObject{
	
	String cName,cFullName,cCapital,cLang,cRel,cGov,cCont,cArea,cPop,cCurrency,cColCode,cURL,cTLD;
	
	public flagObject(String cName,String cFullName,String cCapital,String cLang,String cRel,String cGov,String cCont,String cArea,String cPop,String cCurrency,String cColCode,String cURL,String cTLD){
		this.cName = cName;
		this.cFullName = cFullName;
		this.cCapital = cCapital;
		this.cLang = cLang;
		this.cRel = cRel;
		this.cGov = cGov;
		this.cCont = cCont;
		this.cArea = cArea;
		this.cPop = cPop;
		this.cCurrency = cCurrency;
		this.cColCode = cColCode;
		this.cURL = cURL;
		this.cTLD = cTLD;
	}
}

public class flagDB {
	private static HashMap<String,flagObject> _mapDB = new HashMap<String,flagObject>();
	
	static String countries[] = {
		"Afghanistan","Aland islands","Albania","Algeria","American samoa","Andorra","Angola","Anguilla","Antarctica","Antigua and barbuda",
		"Argentina","Armenia","Aruba","Australia","Austria","Azerbaijan","Bahamas","Bahrain","Bangladesh","Barbados","Belarus","Belgium","Belize","Benin",
		"Bermuda","Bhutan","Biot","Bolivia","Bonaire","Bosnian","Botswana","Bouvet island","Brazil","British antarctic territory","British virgin islands",
		"Brunei","Bulgaria","Burkina faso","Burma","Burundi","Cambodia","Cameroon","Canada","Cape verde","Cascadia","Cayman islands","Central african republic",
		"Chad","Chile","China","Christmas Island","Cocos islands","Colombia","Comoros","Congo","Congo kinshasa","Cook islands","Costa rica","Croatian","Cuba",
		"Curacao","Cyprus","Czech Republic","Denmark","Djibouti","Dominican republic","Dominicana","East Timor","Ecuador","Egypt","El salvador","England",
		"Equatorial guinea","Eritrea","Estonia","Ethiopia","European union","Ex yugoslavia","Falkland islands","Faroe islands","Fiji","Finland","France",
		"French Southern Territories","French polynesia","Gabon","Gambia","Georgia","Germany","Ghana","Gibraltar","Greece","Greenland","Grenada","Guadeloupe",
		"Guam","Guatemala","Guernsey","Guinea bissau","Guinea","Guyana","Haiti","Holy see","Honduras","Hong kong","Hungary","Iceland","India","Indonesia","Iran",
		"Iraq","Ireland","Isle of man","Israel","Italy","Ivory coast","Jamaica","Jan mayen","Japan","Jarvis island","Jersey","Jordan","Kazakhstan","Kenya",
		"Kiribati","Korea","Kosovo","Kuwait","Kyrgyzstan","Laos","Latvia","Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macau",
		"Macedonia","Madagascar","Malawi","Malaysia","Maldives","Mali","Malta","Marshall islands","Martinique","Mauritania","Mauritius","Mayotte","Mexico",
		"Micronesia","Moldova","Monaco","Mongolia","Montenegro","Montserrat","Morocco","Mozambique","Myanmar","Namibia","Nauru","Nepal","Netherlands antilles",
		"Netherlands","New Zealand","New caledonia","Nicaragua","Niger","Nigeria","Niue","Norfolk island","North korea","Northern Mariana Islands","Northern ireland",
		"Norway","Oman","Pakistan","Palau","Palestinian Territory","Panama","Papua new guinea","Paraguay","Peru","Philippines","Pitcairn","Poland","Portugal",
		"Puerto rico","Qatar","Reunion","Romania","Russia","Rwanda","Saint Pierre And Miquelon","Saint Vincent And The Grenadines","Saint Helena",
		"Saint Helena and Dependencies","Saint Pierre and Miquelon","Saint barthelemy","Saint kitts and nevis","Saint lucia","Saint martin","Samoa","San marino",
		"Sao Tome and Principe","Saudi arabia","Scotland","Senegal","Serbia","Seychelles","Sierra leone","Singapore","Sint Eustatius","Sint Maarten","Slovakia",
		"Slovenia","Smom","Solomon islands","Somalia","South Georgia","South Sudan","South africa","Spain","Spm","Sri lanka","Sudan","Suriname","Svalbard","Svg",
		"Swaziland","Sweden","Switzerland","Syria","Taiwan","Tajikistan","Tanzania","Thailand","Timor leste","Togo","Tokelau","Tonga","Trinidad and tobago",
		"Tunisia","Turkey","Turkmenistan","Turks and Caicos Islands","Tuvalu","Uganda","Ukraine","United arab emirates","United kingdom","United states","Uruguay",
		"Uzbekistan","Vanuatu","Vatican city","Venezuela","Vietnam","Virgin islands","Wales","Wallis and futuna","Western sahara","Yemen","Zambia","Zimbabwe"};
	
	static String countryDB[] = {
			"Afghanistan,Islamic Republic of Afghanistan,kabul,Pashto Dari,Islam,Unitary presidential republic,Asia,652864,31108077,Afghani,+93,http://en.m.wikipedia.org/wiki/Afghanistan,.af",
			"Aland Islands,Landskapet Aland,Mariehamn,Swedish,#,Autonomous region of Finland,Europe,1580,28666,Euro,+358,http://en.m.wikipedia.org/wiki/%C3%85land_Islands,.ax",
			"Albania,Republic of Albanian,Tirana,Albanian,#,Parlimentry Republic,Europe,28748,2831741,lek,+355,http://en.m.wikipedia.org/wiki/Albania,.al",
			"Algeria,People's Democratic Republic Of Algeria,Algiers,Arabic,#,Semi-presidential republic,Africa,2381740km,34895000,Algerian dinar,+213,http://en.m.wikipedia.org/wiki/Algeria,.dz ",
			"American Samoa,Amerika Sāmoa,Pago Pago,English/Samoan,#,United States unincorporated territory,#,199,55519,United States dollar,1684,http://en.m.wikipedia.org/wiki/American_Samoa,.as ",
			"Andorra,Principality of Andorra,Andorra la Vella,Catalan,#,Constitunal manarchy,Europe,468,91023,Euro,+376,http://en.m.wikipedia.org/wiki/Andorra,.ad ",
			"Angola,Republic of Angola,Luanda,Portuguese,#,Presidential republic,Africa,1246700,18498000,Kwanza,+244,http://en.m.wikipedia.org/wiki/Angola,.ao  ",
			"Anguilla,#,The Valley,English,#,British overseas territory,#,91,13600,East caribbean dollar,+1-264,http://en.wikipedia.org/wiki/Anguilla,.al  ",
			"Antarctica,#,#,#,#,#,#,14000000,Uninhabited,#,#,http://en.wikipedia.org/wiki/Antarctica,#,",
			"Antigua And Barbuda,#,Saint John's,English,#,Parliamentary democracy under constitutional monarchy,#,440,81799,East Caribbean dollar,+1-268,http://en.wikipedia.org/wiki/Antigua_and_Barbuda,.ag",
			"Argentina,Argentine Republic,Buenos Aires,Spanish,#,Presidential republic,South America,2766890,40091359,Peso,+54,http://en.wikipedia.org/wiki/Argentina,.ar",
			"Armenia,Republic of Armenia,Yerevan,Armenian,#,presidential republic,Asia,29743,3262200,Dram,+374,http://en.wikipedia.org/wiki/Armenia,.am",
			"Bahamas,Commonwealth of the Bahamas,Nassau,English,#,constitutional monarchy,North America,13939,353658,Bahamian dollar,+1-242,http://en.wikipedia.org/wiki/The_Bahamas,.bs",
			"Bahrain,Kingdom of Bahrain,Manama,Arabic,#,constitutional monarchy,Asia,750,1243571,Bahraini dinar,+973,http://en.wikipedia.org/wiki/The_Bahamas,.bh",
			"Bangladesh,People’s Republic of Bangladesh,Dhaka,Bangla,#,Parliamentary republic,Asia,144000,142319000,Taka,+880,http://en.wikipedia.org/wiki/Bangladesh,.bd",
			"Barbados,Barbados,Bridgetown,English,#,Constitutional monarchy,North America,431,284589,Barbadian dollar,+1-246,http://en.wikipedia.org/wiki/Barbados,.bb",
			"Belarus,republic of Belarus,Minsk,Belarussian/Russian,#,presidential republic,Europe,207600,9465400,Belarussian ruble,+375,http://en.wikipedia.org/wiki/Belarus,.by",
			"Belgium,Kingdom of Belgium,Brussels,Dutch/French/German,#,constitutional monarchy,Europe,30528,11071483,Euro,+32,http://en.wikipedia.org/wiki/Belgium,.be",
			"Belize,belize,Belmopan,English,#,Constitutional monarchy,North America,22966,307899,Belize dollar,+501,http://en.wikipedia.org/wiki/Belize,.bz",
			"Benin,Republic of Benin,Porto-Novo,French,#,Presidential republic,Africa,112622,8935000,West African CFA Franc,+229,http://en.wikipedia.org/wiki/Benin,.bj",
			"Bermuda,#,Hamilton,English,#,Parliamentary democracy,#,53.2,64237,Bermudian dollar,+1-441,http://en.wikipedia.org/wiki/Bermuda,.bm",
			"Bhutan,Kingdom of Bhutan,Thimphu,Dzongkha,#,Unitary parliamentary constitutional monarchy,#,38394,742737,Bhutanese ngultrum,+975,http://en.wikipedia.org/wiki/Bhutan,.bt",
			"Biot,British Indian Ocean Territory,Diego Garcia,English,#,British Overseas Territory,#,54400,3000,UK Pound,+246,http://en.wikipedia.org/wiki/British_Indian_Ocean_Territory,.lo",
			"Bolivia,Plurinational State of Bolivia,Sucre La Paz,Spanish/Quechua/Aymara/Guarani,#,Unitary Presidential Constitutional Republic,#,1098581,10556102,Boliviano,+591,http://en.wikipedia.org/wiki/Bolivia,.bo",
			"Bosnia and Herzegovina,Bosnia and Herzegovina,Sarajevo,Bosnian/Croatian/Serbian,#,Federal Parliamentary Republic,#,51197,3871643,Convertible mark,+387,http://en.wikipedia.org/wiki/Bosnia_and_Herzegovina,.ba",
			"Botswana,Republic of Botswana,Gaborone,English/Setswana,#,Unitary Parliamentary Republic,#,581730,2155784,Pula,+267,http://en.wikipedia.org/wiki/Botswana,.bw",
			"Bouvet,Bouvet Island,#,#,#,Dependent Territory,#,49,Uninhabited,#,#,http://en.wikipedia.org/wiki/Bouvet_Island,.bv",
			"Brazil,Federative Republic of Brazil,Brasilia,Portuguese,#,Federal Presidential Constitutional Republic,#,8515767,201032714,Real,+55,http://en.wikipedia.org/wiki/Brazil,.br",
			"British Antarctic Territory,British Antarctic Territory,Rothera,English,#,British Overseas Territory,#,1709400,250,Pound sterling,#,http://en.wikipedia.org/wiki/British_Antarctic_Territory,.aq  ",
			"British Virgin Island,Virgin Island,Road Town,English,#,British Overseas Territory,#,153,27800,United States Dollar,+1-284,http://en.wikipedia.org/wiki/British_Virgin_Islands,.vg",
			"Brunei,Nation of Brunei,Bandar Seri Begawan,Malay/English,#,United Islamic Absolute Monarchy,#,5765,415717,Brunei Dollar,+673,http://en.wikipedia.org/wiki/Brunei,.bn",
			"Bulgaria,Republic of Bulgaria,Sofia,Bulgarian,#,Unitary parliamentary Republic,#,110994,7364570,Lev,+359,http://en.wikipedia.org/wiki/Bulgaria,.bg",
			"Burkina Faso,Burkina Faso,Quagadougou,French/Moore/Mandinka/Bambara,#,Semi-Presidential Republic,#,274200,15730977,West African CFA franc,+226,http://en.wikipedia.org/wiki/Burkina_Faso,.bf",
			"Burma,Republic of the Union Myanmar,Naypyidaw,Burmese,#,Unitary Presidential Constitutional Republic,#,676578,61120000,Kyat,+95,http://en.wikipedia.org/wiki/Burma,.mm",
			"Burundi,Republic of Burundi,Bujumbura,Kirundi/French,#,Presidential Republic,#,27834,8749000,Burundian Franc,+257,http://en.wikipedia.org/wiki/Burundi,.bi",
			"Cambodia,Kingdom of Cambodia,Phnom Penh,Khmer,#,Unitary Parliamentary Constitutional Monarchy,#,181035,15205539,Riel,+855,http://en.wikipedia.org/wiki/Cambodia,.kh",
			"Cameroon,Republic of Cameroon,Yaounde,French/English,#,Dominant-party presidential republic,#,475442,22534532,Centeral African CFA franc,+237,http://en.wikipedia.org/wiki/Cameroon,.cm",
			"Canada,Canada,Ottawa,English/French,#,Federal Parliamentary Constitutional Monarchy,#,9984670,35427524,Canadian dollar,+1,http://en.wikipedia.org/wiki/Canada,.ca",
			"Cape Verde,Republic of Cape Verde,Praia,Portuguese,#,Unitary semi-presidential republic,#,4033,512096,Cape Verdean escudo,+238,http://en.wikipedia.org/wiki/Cape_Verde,.cv",
			"Cayman Island,Cayman Island,George Town,English,#,British Overseas Territory,#,264,56732,Cayman Islands Dollar,+1-345,http://en.wikipedia.org/wiki/Cayman_Islands,.ky",
			"Central African Republic,Central African Republic,Bangui,Sango/French,#,Provisional Republic,#,622984,4422000,Central African CFA,+236,http://en.wikipedia.org/wiki/Central_African_Republic,.cf",
			"Chad,Republic of Chad,N’Djamena,French/Arabic,#,Dominant-party presidential republic,#,1284000,10329208,Central African CFA franc,+235,http://en.wikipedia.org/wiki/Chad,.td",
			"Chile,Republic of Chile,Santiago,Spanish,#,Unitary presidential constitutional republic,#,756096.3,17772871,Peso,+56,http://en.wikipedia.org/wiki/Chile,.cl",
			"China,People’s Republic of China,Beijing,Standard Chinese,#,Single-party socialist state,#,9596961,1350695000,Renminbi,+86,http://en.wikipedia.org/wiki/China,.cn",
			"Christmas Island,Territory of Christmas Island,Flying Fish Cove,English,#,Federal constitutional monarchy,#,135,2072,Australian Dollar,+61,http://en.wikipedia.org/wiki/Christmas_Island,.cx",
			"Cocos(Keeling) Island,Territory of the Cocos(Keeling) Island,West Island,English,#,Federal constitutional monarchy,#,14,596,Australian dollar,61891,http://en.wikipedia.org/wiki/Cocos_(Keeling)_Islands,.cc",
			"Colombia,Republic of Colombia,Bogota,Spanish,#,Unitary presidential constitutional republic,#,1141748,47425437,Peso,+57,http://en.wikipedia.org/wiki/Colombia,.co"
						};
		
	public flagDB() {
		
		String [] cSplit = new String[14];	
		
		for(int i=0;i<countries.length;i++){
			countries[i] = countries[i].toLowerCase().replace(" ","_");
		}
		for(int i=0;i<countryDB.length;i++){
			cSplit = countryDB[i].toLowerCase().replace(" ","_").split(",");
			try{
				_mapDB.put(cSplit[0],new flagObject(			
									cSplit[0],
									cSplit[1].equals("#") ? cSplit[0] : cSplit[1],
									cSplit[2].equals("#") ? "-" : cSplit[2],
									cSplit[3].equals("#") ? "-" : cSplit[3],
									cSplit[4].equals("#") ? "-" : cSplit[4],
									cSplit[5].equals("#") ? "-" : cSplit[5],
									cSplit[6].equals("#") ? "-" : cSplit[6],
									cSplit[7].equals("#") ? "-" : cSplit[7],
									cSplit[8].equals("#") ? "-" : cSplit[8],
									cSplit[9].equals("#") ? "-" : cSplit[9],
									cSplit[10].equals("#") ? "-" : cSplit[10],
									cSplit[11].equals("#") ? "-" : cSplit[11],
									cSplit[12].equals("#") ? "-" : cSplit[12]
								)
			);
			}catch(ArrayIndexOutOfBoundsException e){}
		}
	}
	
	public HashMap<String,flagObject> getMapDB(){
		return _mapDB;
	}
}