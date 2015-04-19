<?php

session_cache_limiter(false);
session_start();

class db
{

    private static $pdo = null;


    /**
     * @return PDO
     */
    public static function conn()
    {
        if (is_null(self::$pdo)) {
            self::$pdo =
                new PDO('mysql:host=srv0.sknz.info;dbname=fed', 'fed', 'fedup');
            self::$pdo->exec('SET CHARACTER SET utf8');
            self::$pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            self::$pdo->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE,
                PDO::FETCH_ASSOC);
        }

        return self::$pdo;
    }
}

db::conn(); // Initialize the database connection straight away

use Slim\Slim;

require 'vendor/autoload.php';
$app = new Slim([
//    'debug' => true
]);

function error(array $errors = ['An unknown error happened '],
               $errorCode = 200)
{
    Slim::getInstance()->contentType('application/json');
    Slim::getInstance()->halt(200,
        json_encode([
            'success' => false,
            'errors' => $errors
        ]));
}

function ok($response = null)
{
    $response =
        $response != null
            ? array_merge(['success' => true], $response)
            : ['success' => true];

    Slim::getInstance()->contentType('application/json');
    Slim::getInstance()->response->body(json_encode($response));
}

function auth()
{
    if (!isset($_SESSION['auth'])) {
        error(['Authentication required']);
    }
}

$app->error(function (\Exception $e) use ($app) {
    error();
});

$app->post('/scenario/new', function () use ($app) {
    $jsonBody = json_decode($app->request->getBody());
    if (!isset($jsonBody->scenario)) {
        error(['Invalid data format']);
    }

    $scenario = $jsonBody->scenario;
    if (!isset($scenario->entry) || empty($scenario->entry)) {
        error(["No entry module"]);
        return;
    }

    if (!isset($scenario->process) || empty($scenario->process)) {
        error(["No processing module"]);
        return;
    }

    $db = db::conn();
    try {
        $stmt = $db->prepare(<<<'SQL'
INSERT INTO scenario
(id, entryType, processType)
VALUES (DEFAULT, ?, ?);
SQL
        )->execute($scenario->entry, $scenario->process);

        ok(['id' => $db->lastInsertId()]);
    } catch (PDOException $e) {
        error(['Unknown database error']);
    }
});

$app->run();
