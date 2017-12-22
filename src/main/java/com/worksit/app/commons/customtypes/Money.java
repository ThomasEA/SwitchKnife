package com.worksit.app.commons.customtypes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import static java.math.BigDecimal.ZERO;

/**
 * Created by Everton on 28/07/2016.
 *
 * Representação de valores monetários
 *
 * O método {@Link init} deve ser chamado uma única vez, geralmente no início
 * do programa. Não será possível utilizar a classe <em>Money</em> sem que esta seja inicializada.
 */
public class Money {

    //region "Exceptions"

    /**
     * Exceção padrão lançada
     */
    public static final class MoneyException extends RuntimeException {
        MoneyException(String aMessage){
            super(aMessage);
        }
    }

    /**
     * Exceção lançada em operações com moedas distintas
     */
    public static final class MismatchedCurrencyException extends RuntimeException {
        MismatchedCurrencyException(String aMessage){
            super(aMessage);
        }
    }

    //endregion

    //region "Private variables"

    /**
     * Indicador de inicialização
     */
    private static boolean initialized = false;

    /**
     * Padrão monetário
     */
    private static Currency DEFAULT_CURRENCY;

    /**
     * Padrão de arredondamento
     */
    private static RoundingMode DEFAULT_ROUNDING_MODE;

    /**
     * Moeda
     */
    private Currency fCurrency;

    /**
     * Arredonamento
     */
    private RoundingMode fRoundingMode;

    /**
     * Valor do fAmount
     */
    private BigDecimal fAmount;

    /**
     * Casas decimais
     */
    private int scale;

    //endegion

    /**
     * Método de inicialização.
     * Insere o padrão de moeda e arredondamento.
     *
     * Este método deve ser chamado apenas uma vez durante a execução da aplicação
     * @param currency Padrão de moeda
     * @param roundingMode Padrão de arredontamento
     * @exception MoneyException Ocorre quando a classe já foi inicializada.
     */
    public static void init(Currency currency, RoundingMode roundingMode) throws MoneyException {
        if (initialized)
            throw new MoneyException("Money parameters have been initialized!");

        DEFAULT_CURRENCY = currency;
        DEFAULT_ROUNDING_MODE = roundingMode;

        initialized = true;
    }

    //region Construtores

    public Money(BigDecimal value) throws MoneyException {
        this(value, -1);
    }

    public Money(BigDecimal value, int scale) throws MoneyException {
        this(value, scale,DEFAULT_CURRENCY, DEFAULT_ROUNDING_MODE);
    }

    public Money(BigDecimal value, RoundingMode roundingMode) throws MoneyException {
        this(value, -1, DEFAULT_CURRENCY, roundingMode);
    }

    public Money(BigDecimal value, int scale, RoundingMode roundingMode) throws MoneyException {
        this(value, scale, DEFAULT_CURRENCY, roundingMode);
    }

    public Money(BigDecimal value, Currency currency, RoundingMode roundingMode) throws MoneyException {
        this(value, currency.getDefaultFractionDigits(),currency, roundingMode);
    }

    public Money(BigDecimal value, int scale, Currency currency, RoundingMode roundingMode) throws MoneyException {
        build(currency, roundingMode, value, scale);
    }

    public Money(double value) throws MoneyException {
        this(value, -1, DEFAULT_CURRENCY, DEFAULT_ROUNDING_MODE);
    }

    public Money(double value, int scale) throws MoneyException {
        this(value, scale, DEFAULT_CURRENCY, DEFAULT_ROUNDING_MODE);
    }

    public Money(double value, RoundingMode roundingMode) throws MoneyException {
        this(value, -1, DEFAULT_CURRENCY, roundingMode);
    }

    public Money(double value, int scale, RoundingMode roundingMode) throws MoneyException {
        this(value, scale, DEFAULT_CURRENCY, roundingMode);
    }

    public Money(double value, Currency currency, RoundingMode roundingMode) throws MoneyException {
        this(value, -1, currency, roundingMode);
    }

    public Money(double value, int scale, Currency currency, RoundingMode roundingMode) throws MoneyException {
        BigDecimal bd = BigDecimal.valueOf(value);
        build(currency, roundingMode, bd, scale);
    }

    //endregion

    /**
     * Configuração de moeda da classe
     */
    public Currency getCurrency() { return fCurrency; }

    /**
     * Retorna o valor montante
     */
    public BigDecimal getAmount() {
        return fAmount;
    }

    /**
     * Retorna
     * {@link #getAmount()}.getPlainString() + espaço + {@link #getCurrency()}.getSymbol().
     * @return "R$ 2.34"
     */
    public String toString(){
        return fCurrency.getSymbol() + " " + fAmount.toPlainString();
    }

    /**
     * Retorna o símbolo da moeda
     * @return
     */
    public String getSymbol() {return fCurrency.getSymbol(); }


    /**
     * Retorna
     * Montante no formato string (sem símbolo de moeda)
     * @return "2.34"
     */
    public String toPlainString() {
        return fAmount.toPlainString();
    }

    /**
     * Retorna flag de inicialização
     * @return <em>true</em> para inicializado e <em>false</em> caso contrário
     */
    public static boolean isInitialized() { return initialized;}

    /**
     * Retorna <tt>true</tt> somente se <tt>obj</tt> <tt>Money</tt> possui a mesma configuração
     * de moeda que o objeto <tt>Money</tt> que chamou o método.
     */
    public boolean isSameCurrencyAs(Money obj){
        boolean result = false;
        if ( obj != null ) {
            result = this.fCurrency.equals(obj.fCurrency);
        }
        return result;
    }

    /** Retorna <tt>true</tt> somente se o montante é positivo. */
    public boolean isPositive(){
        return fAmount.compareTo(ZERO) > 0;
    }

    /** Retorna <tt>true</tt> somente se o montante é negativo. */
    public boolean isNegative(){
        return fAmount.compareTo(ZERO) <  0;
    }

    /** Retorna <tt>true</tt> somente se o montante é igual a zero. */
    public boolean isZero(){
        return fAmount.compareTo(ZERO) ==  0;
    }

    /** Retornar o valor absoluto do montante. */
    public Money abs(){
        return isPositive() ? this : times(-1);
    }

    /** Retorna o montante * (-1). */
    public Money negate(){
        return times(-1);
    }

    //region Operações sobre o montante

    /**
     * Soma <tt>obj</tt> <tt>Money</tt> ao montente em <tt>Money</tt>.
     * Devem possuir o mesmo padrão monetário.
     */
    public Money add(Money obj){
        if (obj == null) return this;
        checkCurrenciesMatch(obj);
        return new Money(fAmount.add(obj.fAmount), this.scale, fCurrency, fRoundingMode);
    }

    /**
     * Subtrai <tt>obj</tt> <tt>Money</tt> do montante em <tt>Money</tt>.
     * Devem possuir o mesmo padrão monetário.
     */
    public Money subtract(Money obj){
        if (obj == null) return this;
        checkCurrenciesMatch(obj);
        return new Money(fAmount.subtract(obj.fAmount), this.scale, fCurrency, fRoundingMode);
    }

    /**
     * Multiplica o montante <tt>Money</tt> por um inteiro.
     */
    public Money times(int aFactor){
        BigDecimal factor = new BigDecimal(aFactor);
        BigDecimal newAmount = fAmount.multiply(factor);
        return new Money(newAmount, fCurrency, fRoundingMode);
    }

    /**
     * Multiplica o montante <tt>Money</tt> por um decimal.
     */
    public Money times(double aFactor){
        BigDecimal newAmount = fAmount.multiply(asBigDecimal(aFactor));
        newAmount = newAmount.setScale(fCurrency.getDefaultFractionDigits(), fRoundingMode);
        return  new Money(newAmount, fCurrency, fRoundingMode);
    }

    /**
     * Divide o montante <tt>Money</tt> por um divisor inteiro.
     * O retorno é um array que contém no indíce 0 o resultado da divisão e no índice 1 o resto,
     * mesmo que este seja 0 (zero).
     */
    public Money[] div(int aDivisor){
        BigDecimal divisor = new BigDecimal(aDivisor);
        BigDecimal result = fAmount.divide(divisor, RoundingMode.HALF_DOWN);

        Money remainder = new Money(fAmount.subtract(result.multiply(divisor)), fCurrency, fRoundingMode);

        return new Money[]{ new Money(result, fCurrency, fRoundingMode), remainder };
    }

    /**
     * Divide o montante <tt>Money</tt> por um divisor decimal.
     * O retorno é um array que contém no indíce 0 o resultado da divisão e no índice 1 o resto,
     * mesmo que este seja 0 (zero).
     */
    public Money[] div(double aDivisor){
        BigDecimal result = fAmount.divide(asBigDecimal(aDivisor), RoundingMode.HALF_DOWN);
        Money remainder = new Money(fAmount.subtract(result.multiply(BigDecimal.valueOf(aDivisor))), fCurrency, fRoundingMode);

        return new Money[]{ new Money(result, fCurrency, fRoundingMode), remainder };
    }

    //endregion

    //region Operações de comparação

    /**
     * Comparação de objetos <em>Money</em>. Essa comparação valida a quantidade de dígitos
     * decimais.
     *
     * Por exemplo, <tt>10</tt> <em>não é</em> igual a <tt>10.00</tt>
     * O método {@link #eq(Money)},por outro lado, <em>não</em>
     * valida a quantidade de decimais.
     */
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (! (obj instanceof Money) ) return false;
        Money that = (Money)obj;
        //the object fields are never null :
        boolean result = (this.fAmount.equals(that.fAmount) );
        result = result && (this.fCurrency.equals(that.fCurrency) );
        result = result && (this.fRoundingMode == that.fRoundingMode);
        return result;
    }

    /**
     * Teste de igualdade para o montante.
     *
     * <P>Retorna <tt>true</tt> somente se ambos os montantes são iguais.
     * Além disso, os padrões monetários devem coincidir.
     * Este método <em>não</em> é sinônimo ao método <tt>equals</tt>.
     */
    public boolean eq(Money obj) {
        checkCurrenciesMatch(obj);
        return compareAmount(obj) == 0;
    }

    /**
     * Maior que.
     *
     * <P>Retorna <tt>true</tt> somente se o montante for maior que o montante em
     * 'obj'. Padrões monetários também devem ser iguais.
     */
    public boolean gt(Money obj) {
        checkCurrenciesMatch(obj);
        return compareAmount(obj) > 0;
    }

    /**
     * Maior que ou igual à.
     *
     * <P>Retorna <tt>true</tt> somente se o montante for maior ou igual ao montante em
     * 'obj'. Padrões monetários tambpem devem ser iguais
     */
    public boolean gteq(Money obj) {
        checkCurrenciesMatch(obj);
        return compareAmount(obj) >= 0;
    }

    /**
     * Menor que.
     *
     * <P>Retorna <tt>true</tt> somente se o montante for menor que o montante em
     * 'obj'. Padrões monetários também devem ser iguais.
     */
    public boolean lt(Money obj) {
        checkCurrenciesMatch(obj);
        return compareAmount(obj) < 0;
    }

    /**
     * Menor que ou igual à.
     *
     * <P>Retorna <tt>true</tt> somente se o montante for menor que ou igual ao montante em
     * 'obj'. Padrões monetários também devem ser iguais.
     */
    public boolean lteq(Money obj) {
        checkCurrenciesMatch(obj);
        return compareAmount(obj) <= 0;
    }

    //endregion

    private final void build(Currency currency, RoundingMode roundingMode, BigDecimal value, int scale) throws MoneyException {
        checkInitialized();
        fCurrency = currency;
        fRoundingMode = roundingMode;

        if (scale == -1) {
            fAmount = value.setScale(fCurrency.getDefaultFractionDigits(), fRoundingMode);
            this.scale = fCurrency.getDefaultFractionDigits();
        }
        else if (value.scale() != scale) {
            fAmount = value.setScale(scale, roundingMode);
            this.scale = scale;
        }
        else {
            fAmount = value;
            this.scale = fAmount.scale();
        }
    }

    private void checkInitialized() throws MoneyException{
        if (!initialized)
            throw new MoneyException("Money parameters need to be initialised.");
    }

    private void checkCurrenciesMatch(Money obj){
        if (! this.fCurrency.equals(obj.getCurrency())) {
            throw new MismatchedCurrencyException(
                    obj.getCurrency() + " doesn't match the expected currency : " + fCurrency
            );
        }
    }

    /** Compara os montantes, ignorando a quantidade de dígitos decimais */
    private int compareAmount(Money obj){
        return this.fAmount.compareTo(obj.fAmount);
    }

    private BigDecimal asBigDecimal(double aDouble){
        //String asString = Double.toString(aDouble);
        //return new BigDecimal(asString);
        return BigDecimal.valueOf(aDouble);
    }

    //region Serialização

    /** @serial */
    private int fHashCode;
    private static final int HASH_SEED = 23;
    private static final int HASH_FACTOR = 37;

    private static final long serialVersionUID = 7526471155622776147L;

    /**
     * Always treat de-serialization as a full-blown constructor, by
     * validating the final state of the de-serialized object.
     */
    private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException {
        aInputStream.defaultReadObject();
        fAmount = new BigDecimal( fAmount.toPlainString() );
    }

    private void writeObject(ObjectOutputStream aOutputStream) throws IOException {
        aOutputStream.defaultWriteObject();
    }

    public int hashCode(){
        if ( fHashCode == 0 ) {
            fHashCode = HASH_SEED;
            fHashCode = HASH_FACTOR * fHashCode + fAmount.hashCode();
            fHashCode = HASH_FACTOR * fHashCode + fCurrency.hashCode();
            fHashCode = HASH_FACTOR * fHashCode + fRoundingMode.hashCode();
        }
        return fHashCode;
    }

    //end region
}
