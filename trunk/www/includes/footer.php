<? if (!isset($noTop)) { ?>
</td>
  </tr>
  <tr>
    <td>
    <br>
<br>
<br>
<br>

	<div id=footer>UCSD Student Run Free Clinic Project - Teams in Engineering Service Prototype</div></td>
  </tr>
</table>



</div>
<? } ?>





<script type="text/javascript" language="javascript" charset="utf-8">
// for top search bar

new Ajax.Autocompleter('name','autocomplete', 'autocomplete.php' );


function highlight (id) {
var nodes = document.getElementsByClassName('data_row_'+id);
for (var n = 0; n < nodes.length; n++) {
nodes[n].style.backgroundColor='#eeeeee';
}
}


function unhighlight (id) {
var nodes = document.getElementsByClassName('data_row_'+id);
for (var n = 0; n < nodes.length; n++) {
nodes[n].style.backgroundColor='white';
}
}
</script>
</body></html>