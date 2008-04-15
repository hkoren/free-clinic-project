	<?php
	
	
	/*
	 * Title: validator.mod.php
	 * Purpose: Validation class. 
	 * 
	 * Author: Jon Campbell
	 * Created: May 16, 2007
	 * Last Updated: May 16, 2007
	 * 
	 */
	
	class Validator {
		private $aValidators = array ();
		function &addValidator($sFormField, $iValidateType = -1, $sErrorMessage = "") {
			$this->aValidators[$sFormField] = new ValidatorElement($sFormField, $iValidateType, $sErrorMessage);	
			return $this->aValidators[$sFormField];
		}
	
		function &getValidator($sFormField) {
			$this->aValidators[$sFormField] = new ValidatorElement($sFormField, -1);
			return $this->aValidators[$sFormField];
		}
	
		function getAllValidators() {
			return $this->aValidators;
		}
	
		function validateAll() {
			$aFailed = array ();
			foreach ($this->aValidators as $oValidatorElement) {
				if (!$oValidatorElement->validate()) {
					$aFailed[$oValidatorElement->getFormField()] = $oValidatorElement->getErrorMessage();
				}
			}
			return $aFailed;
		}
	
		function validate($sFormField) {
			
			if (!array_key_exists($sFormField, $this->aValidators))
				return true;
							
			return ($this->aValidators[$sFormField]->validate());
	
		}
	
	}
	
	class ValidatorElement {
	
		private $sFormField;
		private $sErrorMessage;
		private $aValidateTypes = array ();
	
		/* validation types:
		 * 	0 - nonempty
		 *  1 - alpha
		 *  2 - num
		 *  3 - email
		 *  4 - phone
		 *  5 - date
		 *  6 - len
		 *  additional validators can be added
		 */
	
		function ValidatorElement($sFormField = "", $iValidateType = -1, $sErrorMessage = "") {
			
			$this->sFormField = $sFormField;
			
			if ($iValidateType == -1)
				return;
			
			if (is_array($iValidateType)) {
				foreach ($iValidateType as $iType) {
					$this->addValidationType($iType);
				}
			} else {
				$this->addValidationType($iValidateType);
			}
	
			
			$this->sErrorMessage = $sErrorMessage;
		}
	
		function setFormField($sFormField) {
			$this->sFormField = $sFormField;
		}
		function getFormField() {
			return $this->sFormField;
		}
	
		function setErrorMessage($sErrorMessage) {
			$this->sErrorMessage = $sErrorMessage;
		}
		function getErrorMessage() {
			return $this->sErrorMessage;
		}
	
		function setParameter($sParam) {
			$this->sParamater = $sParam;
		}
		function getParameter() {
			return $this->sParamater;
		}
	
		function addValidationType($iValidateType, $sParam = "") {
			if (is_string($iValidateType)) {
				switch ($iValidateType) {
					case "nonempty" :
						$iValidateType = 0;
						break;
					case "req" :
						$iValidateType = 0;
						break;
					case "alpha" :
						$iValidateType = 1;
						break;
					case "num" :
						$iValidateType = 2;
						break;
					case "email" :
						$iValidateType = 3;
						break;
					case "phone" :
						$iValidateType = 4;
						break;
					case "alphanum" :
						$iValidateType = 5;
						break;
					case "lt" :
						$iValidateType = 6;
						break;
					case "gt" :
						$iValidateType = 7;
						break;
					case "regex" :
						$iValidateType = 8;
						break;
					case "ssn" :
						$iValidateType = 9;
						break;
	
				}
			}
	
			$this->aValidateTypes[][0] = $iValidateType;
			$this->aValidateTypes[count($this->aValidateTypes) - 1][1] = $sParam;
	
		}
	
		function getValue($sFormField = "") {
	
			if ($sFormField != "")
				$this->sFormField = $sFormField;
	
			return $this->cleanPostData(trim($_POST[$this->sFormField]));
		}
	
		function validate() {
	
			$sValue = $this->getValue();
			//echo "{$this->sFormField} = \"$sValue\"<br>";
			foreach ($this->aValidateTypes as $aRow) {
				$iType = $aRow[0];
				$sParam = $aRow[1];
				if (!$this->performValidation($iType, $sValue, $sParam))
					return false;
			}
			return true;
		}
	
		function performValidation($iValidateType, $sValue, $sParam = "") {
	
			$aAlphaArray = array (
			    " ",
				"a",
				"b",
				"c",
				"d",
				"e",
				"f",
				"g",
				"h",
				"i",
				"j",
				"k",
				"l",
				"m",
				"n",
				"o",
				"p",
				"q",
				"r",
				"s",
				"t",
				"u",
				"v",
				"w",
				"x",
				"y",
				"z"
			);
	
			switch ($iValidateType) {
				case 0 :								// NonEmpty / Req
					return (strlen($sValue) > 0) ? true : false;
				case 1 :								// Alpha
					if (strlen($sValue) == 0) 
						return true;					
					for ($i = 0; $i < strlen($sValue); $i++) {
						if (!in_array(strtolower(substr($sValue, $i, 1)),$aAlphaArray))
						{
							return false;
						}
					}
					return true;
				case 2 :								// Num
					return is_numeric(str_replace(array("$",","),"",$sValue)) || (strlen($sValue) == 0);
				case 3 :								// Email
					return $this->isEmail($sValue) || (strlen($sValue) == 0);
				case 4 :								// Phone
					return $this->isPhone($sValue) || (strlen($sValue) == 0);
				case 5 :								// Alphanum
					return $this->isAlphaNum($sValue) || (strlen($sValue) == 0);
				case 6 :								// lte
					return ($sValue <= $sParam);
				case 7 :								// gte
					return ($sValue >= $sParam);
				case 8 :								// regex
					return (strlen($sValue) == 0 || ereg($sParam, $sValue));
				case 9 :								// ssn
					return (strlen($sValue) == 0 || $this->isSSN($sValue));
			}
	
		}
	
		private function isEmail($sEmail) {
			if ((preg_match('/(@.*@)|(\.\.)|(@\.)|(\.@)|(^\.)/', $sEmail)) || (preg_match('/^.+\@(\[?)[a-zA-Z0-9\-\.]+\.([a-zA-Z]{2,3}|[0-9]{1,3})(\]?)$/', $sEmail))) {
				$aHost = explode('@', $sEmail);
				if (checkdnsrr($aHost[1] . '.', 'MX'))
					return true;
				if (checkdnsrr($aHost[1] . '.', 'A'))
					return true;
				if (checkdnsrr($aHost[1] . '.', 'CNAME'))
					return true;
			}
			return false;
		}
		
		private function isSSN($sValue) {
			$sValue = str_replace(array(" ", "-"),"",$sValue);
			
			if (strlen($sValue) == 9 && is_numeric($sValue) && $sValue != "000000000")
				return true;
			return false;	
			
		}
	
		private function isAlphaNum($sValue) {
	
			$aBannedChars = array (
				'@',
				'#',
				'%',
				'^',
				'&',
				'*',
				'~',
				'[',
				']',
				'<',
				'>',
				'+',
				'=',
				'"',
				'|',
				'{',
				'}'
			);
	
			if (strlen($sValue) == 0)
				return false;
			for ($i = 0; $i < strlen($sValue); $i++) {
				if (in_array(substr($sValue, $i, 1), $aBannedChars))
					return false;
			}
			return true;
		}
	
		private function isPhone($sPhone, $bArea = true) {
			if (preg_match("/^[ ]*[(]{0,1}[ ]*[0-9]{3,3}[ ]*[)]{0,1}[-]{0,1}[ ]*[0-9]{3,3}[ ]*[-]{0,1}[ ]*[0-9]{4,4}[ ]*$/", $sPhone) || (preg_match("/^[ ]*[0-9]{3,3}[ ]*[-]{0,1}[ ]*[0-9]{4,4}[ ]*$/", $sPhone) && !$bArea))
				return eregi_replace("[^0-9]", "", $sPhone);
			return false;
		}
	
		private function cleanPostData($data) {
	
			// Address Magic Quotes.
			if (ini_get('magic_quotes_gpc')) {
				$data = stripslashes($data);
			}
	
			// Check for mysql_real_escape_string() support.
			if (function_exists('mysql_real_escape_string')) {
				global $dbc; // Need the connection.
				$data = mysql_real_escape_string(htmlentities(strip_tags(trim($data))), $dbc);
			} else {
				$data = mysql_escape_string(htmlentities(strip_tags(trim($data))));
			}
	
			// Return the escaped value.
			return $data;
	
		}
	
	}
	?>
