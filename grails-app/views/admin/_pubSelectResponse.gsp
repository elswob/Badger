<g:if test="${pubnum1.count[0] == pubnum2.count[0]}">
	<br>There are no new publications to add
</g:if>
<g:else>
	<br>${pubnum2.count[0] - pubnum1.count[0]} new publications have been added to the database
</g:else>