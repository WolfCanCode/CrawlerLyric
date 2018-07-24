<?xml version="1.0" encoding="UTF-8"?>


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/>
    <xsl:param name="callback"/>
    <xsl:param name="page"/>
    <xsl:template match="/">
        <xsl:for-each select="data/songs">
            <xsl:if test="position() &lt;= $page*10 and position() &gt; $page*10-10">
                <div class="itmSg">
                    <xsl:attribute name="id">song<xsl:value-of select="@id"/>
                    </xsl:attribute>
                    <a>
                        <xsl:attribute name="href">
                            song/<xsl:value-of select="@id"/>?callback=<xsl:value-of select="$callback"/>
                        </xsl:attribute>
                        <xsl:attribute name="onclick">effectClick("song<xsl:value-of select="@id"/>")
                        </xsl:attribute>
                        <xsl:value-of select="name"/>
                    </a>
                    <br/>
                    <span>
                        <xsl:value-of select="artist"/>
                    </span>
                    <p>
                        <xsl:value-of select="lyric/verses[@sequence=1]"/>
                    </p>
                    <p>
                        <xsl:value-of select="lyric/verses[@sequence=2]"/>
                    </p>
                    <p>
                        <xsl:value-of select="lyric/verses[@sequence=3]"/>
                    </p>
                    <p>...</p>
                </div>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
